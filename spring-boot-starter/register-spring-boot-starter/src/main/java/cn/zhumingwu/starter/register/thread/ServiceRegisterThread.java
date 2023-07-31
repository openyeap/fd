package cn.zhumingwu.starter.register.thread;

import cn.zhumingwu.base.service.ServiceMetaDataProvider;
import cn.zhumingwu.starter.register.model.ServiceInstanceInfo;
import cn.zhumingwu.starter.register.properties.RegisterProperties;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.event.ServiceDiscoveredEvent;
import cn.zhumingwu.base.properties.ProjectProperties;
import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.starter.register.client.ServiceCenterClient;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ServiceRegisterThread implements SmartLifecycle {

    private final RegisterProperties registerProperties;
    private final ServiceCenterClient eurekaClient;
    private final TaskScheduler taskScheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private ScheduledFuture<?> serviceWatchFuture;
    //    private ScheduledFuture<?> eventWatchFuture;
//    private ScheduledFuture<?> configWatchFuture;
    private ScheduledFuture<?> serviceRegisterFuture;
    private final List<InstanceInfo> services;

    private final List<ServiceMetaDataProvider> serviceMetaDataProviders;

    public ServiceRegisterThread(ProjectProperties projectProperties, RegisterProperties registerProperties,
                                 ServiceCenterClient eurekaClient, List<ServiceMetaDataProvider> serviceMetaDataProviders, TaskScheduler taskScheduler) {
        this.registerProperties = registerProperties;
        this.eurekaClient = eurekaClient;
        this.taskScheduler = taskScheduler;
        this.serviceMetaDataProviders = serviceMetaDataProviders;

        var registry = this.registerProperties.getRegistry();

        if (registry.isEnabled()) {
            Map<String, String> metaData;
            if (this.serviceMetaDataProviders != null) {
                metaData = new TreeMap<>();
                for (var item : this.serviceMetaDataProviders) {
                    metaData.putAll(item.metadata());
                }
            } else {
                metaData = Collections.emptyMap();
            }
            var serviceInfo = InstanceInfo.builder()
                    .name(projectProperties.getName())
                    .ip(projectProperties.getIp())
                    .port(projectProperties.getPort())
                    .schema("http")
                    .metadata(metaData)
                    .build();
            if (registry.getServices() == null) {
                this.services = Collections.singletonList(serviceInfo);
            } else {
                this.services = registry.getServices();
                this.services.add(serviceInfo);
            }
        } else {
            this.services = Collections.emptyList();
        }
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            if (this.registerProperties.getServiceWatch().isEnabled()) {
                this.serviceWatchFuture = this.taskScheduler.scheduleWithFixedDelay(this::serviceWatch, this.registerProperties.getServiceWatch().getDelay());
            }

            if (this.registerProperties.getRegistry().isEnabled()) {
                this.serviceRegisterFuture = this.taskScheduler.scheduleWithFixedDelay(this::serviceRegister, this.registerProperties.getRegistry().getDelay());
            }
        }
    }

    public void serviceRegister() {
        // 判断服务是否已经启动
        if (!this.running.get()) {
            return;
        }
        try {
            var healthCheck = this.registerProperties.getHealthCheck();
            for (var item : this.services) {
                ServiceInstanceInfo service = new ServiceInstanceInfo();
                service.setIpAddr(item.getIp());
                service.setApp(item.getId());
                service.setApp(item.getName());
                service.setPort(item.getPort());
                service.setMetadata(item.getMetadata());
                if (healthCheck.isEnabled()) {
                    service.setHealthCheckUrl(MessageFormat.format("{}://{}:{}/{}", item.getSchema(), item.getIp(), item.getPort(), healthCheck.getPath()));
                }
                this.eurekaClient.registerInstance(service.getApp(), service);
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
            var serviceInstanceInfoList = this.eurekaClient.queryInstances();
            if (serviceInstanceInfoList == null || serviceInstanceInfoList.size() == 0) {
                return;
            }
            Map<String, List<InstanceInfo>> services = new HashMap<>();
            var event = new ServiceDiscoveredEvent(this, null);
            for (var item : serviceInstanceInfoList) {
                var name = item.getApp();
                var serviceModel = InstanceInfo.builder()
                        .port(item.getPort())
                        .ip(item.getIpAddr())
                        .id(item.getApp())
                        .metadata(item.getMetadata())
                        .build();

                if (!services.containsKey(name)) {
                    services.put(name, Collections.singletonList(serviceModel));
                } else {
                    services.get(name).add(serviceModel);
                }
            }
            ApplicationContextHolder.publishLocal(event);
        } catch (
                Exception e) {
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
            if (this.serviceRegisterFuture != null) {
                this.serviceRegisterFuture.cancel(true);
            }
        }
    }
}
