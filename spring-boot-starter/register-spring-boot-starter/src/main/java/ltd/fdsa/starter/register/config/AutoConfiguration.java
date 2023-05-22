package ltd.fdsa.starter.register.config;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.event.RemoteEventPublisher;
import ltd.fdsa.core.lock.LockManager;
import ltd.fdsa.core.properties.ProjectProperties;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.starter.register.client.EurekaClient;
import ltd.fdsa.starter.register.properties.RegisterProperties;
import ltd.fdsa.starter.register.thread.RegisterThread;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@Slf4j
@EnableConfigurationProperties({ RegisterProperties.class })
@EnableRetry
public class AutoConfiguration {

    static final String CONSUL_WATCH_TASK_SCHEDULER_NAME = "registerTaskExecutor";

    @Bean
    @Primary
    public EurekaClient createMultiConsulRawClient(RegisterProperties properties) {
        NamingUtils.formatLog(log, "MultiConsulRawClient Started");
        var rawClient = new EurekaClient(properties);
        return rawClient;
    }

    @Bean
    public RegisterThread consulWatch(ProjectProperties projectProperties, RegisterProperties properties,
                                      EurekaClient eurekaClient, @Qualifier(CONSUL_WATCH_TASK_SCHEDULER_NAME) TaskScheduler taskScheduler) {
        NamingUtils.formatLog(log, "ConsulWatch Started");
        return new RegisterThread(projectProperties, properties, eurekaClient, taskScheduler);
    }

    @Bean(name = CONSUL_WATCH_TASK_SCHEDULER_NAME)
    public TaskScheduler configWatchTaskScheduler() {
        var result = new ThreadPoolTaskScheduler();
        result.setPoolSize(3);
        return result;
    }
}