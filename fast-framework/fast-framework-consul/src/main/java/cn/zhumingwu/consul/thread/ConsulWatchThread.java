
package cn.zhumingwu.consul.thread;

import cn.zhumingwu.consul.properties.ConsulProperties;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.catalog.CatalogServicesRequest;
import com.ecwid.consul.v1.event.EventListRequest;
import com.ecwid.consul.v1.event.model.Event;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.event.RefreshedEvent;
import cn.zhumingwu.base.event.RemotingEvent;
import cn.zhumingwu.base.event.ServiceDiscoveredEvent;
import cn.zhumingwu.base.properties.ProjectProperties;
import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.base.util.YamlUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.ClassUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ConsulWatchThread implements SmartLifecycle {

    private final ConsulProperties consulProperties;
    private final ProjectProperties properties;
    private final ConsulClient consulClient;
    private final TaskScheduler taskScheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<BigInteger> catalogServicesIndex = new AtomicReference<>();
    private final AtomicReference<BigInteger> eventListIndex = new AtomicReference<>();
    private final AtomicReference<BigInteger> configIndex = new AtomicReference<>();
    private ScheduledFuture<?> serviceWatchFuture;
    private ScheduledFuture<?> eventWatchFuture;
    private ScheduledFuture<?> configWatchFuture;
    private ScheduledFuture<?> serviceRegisterFuture;

    public ConsulWatchThread(ProjectProperties projectProperties, ConsulProperties consulProperties,
            ConsulClient consul, TaskScheduler taskScheduler) {
        this.properties = projectProperties;
        this.consulProperties = consulProperties;
        this.consulClient = consul;
        this.taskScheduler = taskScheduler;
        var registry = this.consulProperties.getRegistry();
        if (registry.isEnabled()) {
            if (registry.getServices() == null) {
                registry.setServices(new LinkedList<InstanceInfo>());
            }
            var list = registry.getServices();
            var serviceInfo = InstanceInfo.builder()
                    .name(this.properties.getName())
                    .ip(this.properties.getIp())
                    .port(this.properties.getPort())
                    .schema("http")
                    .build();
            list.add(serviceInfo);
        }
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            if (this.consulProperties.getEventWatch().isEnabled()) {
                this.eventListIndex.set(BigInteger.valueOf(this.consulClient
                        .eventList(EventListRequest.newBuilder().setQueryParams(QueryParams.DEFAULT).build())
                        .getConsulIndex()));
                this.eventWatchFuture = this.taskScheduler.scheduleWithFixedDelay(this::eventWatch,
                        this.consulProperties.getEventWatch().getDelay());
            }
            if (this.consulProperties.getServiceWatch().isEnabled()) {
                this.serviceWatchFuture = this.taskScheduler.scheduleWithFixedDelay(this::serviceWatch,
                        this.consulProperties.getServiceWatch().getDelay());
            }
            if (this.consulProperties.getConfigWatch().isEnabled()) {
                this.configWatchFuture = this.taskScheduler.scheduleWithFixedDelay(this::configWatch,
                        this.consulProperties.getConfigWatch().getDelay());
            }
            if (this.consulProperties.getRegistry().isEnabled()) {
                this.serviceRegisterFuture = this.taskScheduler.scheduleWithFixedDelay(this::serviceRegister,
                        this.consulProperties.getRegistry().getDelay());
            }
        }
    }

    public void serviceRegister() {
        // 判断服务是否已经启动
        if (!this.running.get()) {
            return; 
        }
        try {
            var registry = this.consulProperties.getRegistry();
            if (registry.isEnabled()) {

                var list = registry.getServices();
                var healthCheck = this.consulProperties.getHealthCheck();
                for (var item : list) {
                    NewService service = new NewService();
                    service.setAddress(item.getIp());
                    service.setId(item.getId());
                    service.setName(item.getName());
                    service.setPort(item.getPort());
                    service.setMeta(item.getMetadata());
                    if (healthCheck.isEnabled()) {
                        NewService.Check check = new NewService.Check();
                        check.setHttp(item.getUrl() + healthCheck.getPath());
                        check.setMethod(healthCheck.getMethod());
                        check.setInterval(healthCheck.getDelay().getSeconds() + "s");
                        check.setTimeout(healthCheck.getWaitTime().getSeconds() + "s");
                        check.setDeregisterCriticalServiceAfter(healthCheck.getRemoveAfter().getSeconds() + "s");
                        service.setCheck(check);
                    }
                    this.consulClient.agentServiceRegister(service, this.consulProperties.getAclToken());
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

            CatalogServicesRequest request = CatalogServicesRequest.newBuilder()
                    .setQueryParams(
                            new QueryParams(this.consulProperties.getServiceWatch().getWaitTime().toMillis(), index))
                    .setToken(this.consulProperties.getAclToken())
                    .build();
            Response<Map<String, List<String>>> response = this.consulClient.getCatalogServices(request);
            Long consulIndex = response.getConsulIndex();
            if (consulIndex != null) {
                this.catalogServicesIndex.set(BigInteger.valueOf(consulIndex));
            }

            if (log.isTraceEnabled()) {
                log.trace("Received services update from consul: " + response.getValue() + ", index: " + consulIndex);
            }

            HealthServicesRequest healthServicesRequest = HealthServicesRequest.newBuilder().build();

            Map<String, List<InstanceInfo>> data = new HashMap<>(response.getValue().size());

            for (var serviceName : response.getValue().keySet()) {
                var serviceList = this.consulClient.getHealthServices(serviceName, healthServicesRequest).getValue();
                List<InstanceInfo> list = new ArrayList<>();
                for (HealthService item : serviceList) {
                    var service = item.getService();
                    var address = service.getAddress();
                    if (Strings.isNullOrEmpty(address)) {
                        address = item.getNode().getAddress();
                    }
                    var metadata = service.getMeta();
                    if (metadata == null) {
                        metadata = Collections.emptyMap();
                    }
                    var serviceModel = InstanceInfo.builder()
                            .port(service.getPort())
                            .ip(address)
                            .id(service.getId())
                            .metadata(metadata)
                            .build();
                    list.add(serviceModel);
                }
                data.put(serviceName, list);
            }
            ApplicationContextHolder.publishLocal(new ServiceDiscoveredEvent(this, data));
        } catch (Exception e) {
            log.error("Error watching Consul CatalogServices", e);
        }
    }

    public void eventWatch() {
        // 判断服务是否已经启动
        if (!this.running.get()) {
            return;
        }

        try {
            // 得到最新index
            long index = -1;
            if (this.eventListIndex.get() != null) {
                index = this.eventListIndex.get().longValue();
            }

            // 构建请求
            var request = EventListRequest.newBuilder()
                    .setQueryParams(
                            new QueryParams(this.consulProperties.getEventWatch().getWaitTime().toMillis(), index))
                    .setToken(this.consulProperties.getAclToken())
                    .build();
            Response<List<Event>> response = this.consulClient.eventList(request);
            Long consulIndex = response.getConsulIndex();
            if (consulIndex != null) {
                this.eventListIndex.set(BigInteger.valueOf(consulIndex));
            }
            var events = response.getValue();
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                long eventIndex = event.getWaitIndex();

                if (index == eventIndex) {
                    events = events.subList(i + 1, events.size());
                    break;
                }
            }

            for (Event event : events) {
                if (Strings.isNullOrEmpty(event.getPayload())) {
                    continue;
                }
                var clazz = (Class<ApplicationEvent>) ClassUtils.forName(event.getName(), null);
                if (clazz == null) {
                    continue;
                }
                var payload = new String(
                        Base64.getDecoder().decode(event.getPayload().getBytes(StandardCharsets.UTF_8)));
                var applicationEvent = RemotingEvent.getApplicationEvent(payload, clazz);
                if (applicationEvent == null) {
                    continue;
                }
                ApplicationContextHolder.publishLocal(applicationEvent);
            }
        } catch (Exception e) {
            log.error("Error watching Consul event", e);
        }
    }

    public void configWatch() {
        // 判断服务是否已经启动
        if (!this.running.get()) {
            return;
        }

        try {
            // 得到最新index
            long index = -1;
            if (this.configIndex.get() != null) {
                index = this.configIndex.get().longValue();
            }

            // 构建请求
            String keyPrefix = this.consulProperties.getConfigWatch().getKeyPrefix() + "/" + this.properties.getName();
            var response = this.consulClient.getKVValues(keyPrefix, this.consulProperties.getAclToken(),
                    new QueryParams(this.consulProperties.getConfigWatch().getWaitTime().toMillis(), index));
            Long consulIndex = response.getConsulIndex();
            if (consulIndex != null) {
                this.configIndex.set(BigInteger.valueOf(consulIndex));
            }
            var values = response.getValue();
            if (values == null) {
                return;
            }
            for (var value : values) {
                var key = value.getKey();
                var payload = value.getDecodedValue();
                if (Strings.isNullOrEmpty(payload)) {
                    continue;
                }
                // 目前只支持yaml格式
                if (key.endsWith("yaml")) {
                    RefreshedEvent event = new RefreshedEvent(this, YamlUtils.load(payload));
                    ApplicationContextHolder.publishLocal(event);
                }
            }
        } catch (Exception e) {
            log.error("Error watching Consul config", e);
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
