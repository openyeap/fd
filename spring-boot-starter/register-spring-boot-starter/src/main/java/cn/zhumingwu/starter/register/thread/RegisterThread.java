package cn.zhumingwu.starter.register.thread;

import cn.zhumingwu.starter.register.model.ServiceInstanceInfo;
import cn.zhumingwu.starter.register.properties.RegisterProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.core.context.ApplicationContextHolder;
import cn.zhumingwu.core.event.ServiceDiscoveredEvent;
import cn.zhumingwu.core.properties.ProjectProperties;
import cn.zhumingwu.core.service.ServiceInfo;
import cn.zhumingwu.starter.register.client.EurekaClient;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class RegisterThread implements SmartLifecycle {

    private final RegisterProperties registerProperties;
    private final ProjectProperties properties;
    private final EurekaClient eurekaClient;
    private final TaskScheduler taskScheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<BigInteger> catalogServicesIndex = new AtomicReference<>();
    private final AtomicReference<BigInteger> eventListIndex = new AtomicReference<>();
    private final AtomicReference<BigInteger> configIndex = new AtomicReference<>();
    private ScheduledFuture<?> serviceWatchFuture;
    private ScheduledFuture<?> eventWatchFuture;
    private ScheduledFuture<?> configWatchFuture;
    private ScheduledFuture<?> serviceRegisterFuture;

    public RegisterThread(ProjectProperties projectProperties, RegisterProperties consulProperties,
                          EurekaClient eurekaClient, TaskScheduler taskScheduler) {
        this.properties = projectProperties;
        this.registerProperties = consulProperties;
        this.eurekaClient = eurekaClient;
        this.taskScheduler = taskScheduler;
        var registry = this.registerProperties.getRegistry();
        if (registry.isEnabled()) {
            if (registry.getServices() == null) {
                registry.setServices(new LinkedList<ServiceInfo>());
            }
            var list = registry.getServices();
            var serviceInfo = ServiceInfo.builder()
                    .name(this.properties.getName())
                    .ip(this.properties.getAddress())
                    .port(this.properties.getPort())
                    .schema("http")
                    .build();
            list.add(serviceInfo);
        }
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            if (this.registerProperties.getServiceWatch().isEnabled()) {
                this.serviceWatchFuture = this.taskScheduler.scheduleWithFixedDelay(this::serviceWatch,
                        this.registerProperties.getServiceWatch().getDelay());
            }

            if (this.registerProperties.getRegistry().isEnabled()) {
                this.serviceRegisterFuture = this.taskScheduler.scheduleWithFixedDelay(this::serviceRegister,
                        this.registerProperties.getRegistry().getDelay());
            }
        }
    }

    public void serviceRegister() {
        // 判断服务是否已经启动
        if (!this.running.get()) {
            return;
        }
        try {
            var registry = this.registerProperties.getRegistry();
            if (registry.isEnabled()) {

                var list = registry.getServices();
                var healthCheck = this.registerProperties.getHealthCheck();
                for (var item : list) {
                    ServiceInstanceInfo service = new ServiceInstanceInfo();

                    service.setIpAddr(item.getIp());
                    service.setApp(item.getId());
                    service.setApp(item.getName());
                    service.setPort(item.getPort());
                    service.setMetadata(item.getMetadata());
                    if (healthCheck.isEnabled()) {
//                        NewService.Check check = new NewService.Check();
//                        check.setHttp(item.getUrl() + healthCheck.getPath());
//                        check.setMethod(healthCheck.getMethod());
//                        check.setInterval(healthCheck.getDelay().getSeconds() + "s");
//                        check.setTimeout(healthCheck.getWaitTime().getSeconds() + "s");
//                        check.setDeregisterCriticalServiceAfter(healthCheck.getRemoveAfter().getSeconds() + "s");
//                        service.setCheck(check);
                    }
                    this.eurekaClient.registerInstance(service.getApp(), service);
                }
            }
        } catch (Exception e) {
            log.error("Error Consul register", e);
        }
    }

    public void serviceWatch() {
        // 判断服务是否已经启动
        if (!this.running.get()) {
            return;
        }
        try {
            long index = -1;
            if (this.catalogServicesIndex.get() != null) {
                index = this.catalogServicesIndex.get().longValue();
            }

            var response = this.eurekaClient.queryInstances();
//             if (response != null) {
//                this.catalogServicesIndex.set(BigInteger.valueOf(consulIndex));
//            }

//            if (log.isTraceEnabled()) {
//                log.trace("Received services update from consul: " + response.getValue() + ", index: " + consulIndex);
//            }
//
//            HealthServicesRequest healthServicesRequest = HealthServicesRequest.newBuilder().build();
//
//            Map<String, List<ServiceInfo>> data = new HashMap<>(response.getValue().size());
//
//            for (var serviceName : response.getValue().keySet()) {
//                var serviceList = this.eurekaClient.getHealthServices(serviceName, healthServicesRequest).getValue();
//                List<ServiceInfo> list = new ArrayList<>();
//                for (HealthService item : serviceList) {
//                    var service = item.getService();
//                    var address = service.getAddress();
//                    if (Strings.isNullOrEmpty(address)) {
//                        address = item.getNode().getAddress();
//                    }
//                    var metadata = service.getMeta();
//                    if (metadata == null) {
//                        metadata = Collections.emptyMap();
//                    }
//                    var serviceModel = ServiceInfo.builder()
//                            .port(service.getPort())
//                            .ip(address)
//                            .id(service.getId())
//                            .metadata(metadata)
//                            .build();
//                    list.add(serviceModel);
//                }
//                data.put(serviceName, list);
//            }
            ApplicationContextHolder.publishLocal(new ServiceDiscoveredEvent(this, null));
        } catch (Exception e) {
            log.error("Error watching Consul CatalogServices", e);
        }
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public int getPhase() {
        return 0;
    }

    @Override
    public void stop(Runnable callback) {
        this.stop();
        callback.run();
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            if (this.serviceWatchFuture != null) {
                this.serviceWatchFuture.cancel(true);
            }
            if (this.eventWatchFuture != null) {
                this.eventWatchFuture.cancel(true);
            }
            if (this.configWatchFuture != null) {
                this.configWatchFuture.cancel(true);
            }
            if (this.serviceRegisterFuture != null) {
                this.serviceRegisterFuture.cancel(true);
            }
        }
    }
}
