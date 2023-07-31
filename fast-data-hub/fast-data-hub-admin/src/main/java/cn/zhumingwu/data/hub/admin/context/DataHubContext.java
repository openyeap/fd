package cn.zhumingwu.data.hub.admin.context;

import cn.zhumingwu.base.event.ServiceDiscoveredEvent;
import cn.zhumingwu.base.service.InstanceInfo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class DataHubContext implements ApplicationListener<ServiceDiscoveredEvent>, SmartLifecycle {
    private static final Map<String, List<InstanceInfo>> CACHED_INSTANCE_RESULT = new HashMap<>();
    private static final AtomicBoolean CAN_CACHE_RESULT = new AtomicBoolean(false);

    Lock lock = new ReentrantLock();
    private final AtomicBoolean running = new AtomicBoolean(false);


    public DataHubContext() {

    }

    @Override
    public void onApplicationEvent(ServiceDiscoveredEvent event) {

        List<InstanceInfo> list = new ArrayList<>();
        var services = event.getServices();
        for (var service : services.entrySet()) {
            list.addAll(service.getValue());
        }
        if (list.size() == 0) {
            return;
        }
        lock.lock();
        try {
            CAN_CACHE_RESULT.set(false);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            //
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {

            //
        }
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }
}
