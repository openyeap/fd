package cn.zhumingwu.starter.consul.config;

import cn.zhumingwu.base.event.RemoteEventPublisher;
import cn.zhumingwu.base.lock.LockManager;
import cn.zhumingwu.base.properties.ProjectProperties;
import cn.zhumingwu.base.util.NamingUtils;
import com.ecwid.consul.v1.ConsulClient;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.consul.client.MultiConsulRawClient;
import cn.zhumingwu.consul.event.ConsulEventService;
import cn.zhumingwu.consul.lock.ConsulLockManager;
import cn.zhumingwu.consul.properties.ConsulProperties;
import cn.zhumingwu.consul.thread.ConsulWatchThread;
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
@EnableConfigurationProperties({ ConsulProperties.class })
@EnableRetry
public class ConsulAutoConfiguration {

    static final String CONSUL_WATCH_TASK_SCHEDULER_NAME = "consulWatchTaskExecutor";

    @Bean
    @Primary
    public MultiConsulRawClient createMultiConsulRawClient(ConsulProperties properties) {
        NamingUtils.formatLog(log, "MultiConsulRawClient Started");
        var rawClient = new MultiConsulRawClient(properties);
        return rawClient;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(ConsulClient.class)
    public ConsulClient createConsulClient(MultiConsulRawClient rawClient) {
        NamingUtils.formatLog(log, "ConsulClient Started");
        return new ConsulClient(rawClient);
    }

    @Bean
    @ConditionalOnMissingBean(RemoteEventPublisher.class)
    public RemoteEventPublisher consulEventService(ConsulClient consulClient) {
        NamingUtils.formatLog(log, "RemoteEventPublisher Started");
        return new ConsulEventService(consulClient);
    }

    @Bean
    public ConsulWatchThread consulWatch(ProjectProperties projectProperties, ConsulProperties properties,
                                         ConsulClient consul, @Qualifier(CONSUL_WATCH_TASK_SCHEDULER_NAME) TaskScheduler taskScheduler) {
        NamingUtils.formatLog(log, "ConsulWatch Started");
        return new ConsulWatchThread(projectProperties, properties, consul, taskScheduler);
    }

    @Bean("consul")
    public LockManager consulLockManager(ConsulClient consulClient) {
        return new ConsulLockManager(consulClient);
    }

    @Bean(name = CONSUL_WATCH_TASK_SCHEDULER_NAME)
    public TaskScheduler configWatchTaskScheduler() {
        var result = new ThreadPoolTaskScheduler();
        result.setPoolSize(3);
        return result;
    }
}