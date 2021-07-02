package ltd.fdsa.switcher.core.job.thread;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.context.ApplicationContextHolder;
import ltd.fdsa.core.event.RefreshedEvent;
import ltd.fdsa.core.event.RemotingEvent;
import ltd.fdsa.core.event.ServiceDiscoveredEvent;
import ltd.fdsa.core.properties.ProjectProperties;
import ltd.fdsa.core.service.ServiceInfo;
import ltd.fdsa.core.util.YamlUtils;
import ltd.fdsa.switcher.core.job.coordinator.Coordinator;
import ltd.fdsa.switcher.core.job.enums.RegistryConfig;
import ltd.fdsa.switcher.core.job.executor.JobExecutor;
import ltd.fdsa.switcher.core.job.model.RegistryParam;
import ltd.fdsa.web.view.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.ClassUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ExecutorRegistryThread implements SmartLifecycle {
    private final Properties properties;
    private final TaskScheduler taskScheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<BigInteger> catalogServicesIndex = new AtomicReference<>();
    private ScheduledFuture<?> serviceWatchFuture;
    public ExecutorRegistryThread(Properties properties, TaskScheduler taskScheduler) {
        this.properties = properties;
        this.taskScheduler = taskScheduler;
    }
    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            serviceRegister();
        }
    }

    public void serviceRegister() {
        //判断服务是否已经启动
        if (!this.running.get()) {
            return;
        }
        try {
            var appName = this.properties.getProperty("name");
            if (appName == null || appName.trim().length() == 0) {
                log.warn("project.executor registry config fail, appName is null.");
                return;
            }
            if (JobExecutor.getCoordinators() == null || JobExecutor.getCoordinators().size() <= 0) {
                log.warn("project.executor registry config fail, adminAddresses is null.");
                return;
            }

            var address = this.properties.getProperty("address", "");
            RegistryParam registryParam = new RegistryParam(RegistryConfig.EXECUTOR.name(), appName, address);
            for (Coordinator client : JobExecutor.getCoordinators()) {
                try {
                    Result<String> registryResult = client.registry(registryParam);
                    if (registryResult != null && Result.success().getCode() == registryResult.getCode()) {
                        registryResult = Result.success();
                        log.debug("project.registry success, registryParam:{}, registryResult:{}", new Object[]{registryParam, registryResult});
                        break;
                    } else {
                        log.info("project.registry fail, registryParam:{},registryResult:{}", new Object[]{registryParam, registryResult});
                    }
                } catch (Exception e) {
                    log.info("project.registry error, registryParam:{}", registryParam, e);
                }
            }
        } catch (Exception e) {
            log.error("Error Consul register", e);
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

                // registry remove
                var appName = this.properties.getProperty("name");
                var address = this.properties.getProperty("address");
                try {
                    RegistryParam registryParam = new RegistryParam(RegistryConfig.EXECUTOR.name(), appName, address);
                    for (Coordinator client : JobExecutor.getCoordinators()) {
                        try {
                            Result<String> registryResult = client.registryRemove(registryParam);
                            if (registryResult != null
                                    && Result.success().getCode() == registryResult.getCode()) {
                                registryResult = Result.success();
                                log.info("project.registry - remove success, registryParam:{},registryResult:{}",
                                        new Object[]{registryParam, registryResult});
                                break;
                            } else {
                                log.info("project.registry - remove fail, registryParam:{},registryResult:{}",
                                        new Object[]{registryParam, registryResult});
                            }
                        } catch (Exception e) {
                            log.info("project.registry - remove error, registryParam:{}", registryParam, e);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                log.info("project.executor registry thread destory.");
            }
        }
    }
}


