package cn.zhumingwu.starter.register.config;

import cn.zhumingwu.base.service.ServiceMetaDataProvider;
import cn.zhumingwu.starter.register.properties.RegisterProperties;
import cn.zhumingwu.starter.register.thread.ServiceRegisterThread;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.properties.ProjectProperties;
import cn.zhumingwu.base.util.NamingUtils;
import cn.zhumingwu.starter.register.client.ServiceCenterClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

@Configuration
@Slf4j
@EnableConfigurationProperties({RegisterProperties.class})
@EnableRetry
public class AutoConfiguration {

    static final String SERVICE_CENTER_CLIENT_TASK_EXECUTOR = "ServiceCenterClientTaskExecutor";

    @Bean
    @Primary
    public ServiceCenterClient serviceCenterClient(RegisterProperties properties) {
        NamingUtils.formatLog(log, "Service Center Client initialed");
        return new ServiceCenterClient(properties);
    }

    @Bean
    public ServiceRegisterThread serviceDiscovery(ProjectProperties projectProperties, RegisterProperties properties,
                                                  ServiceCenterClient eurekaClient,
                                                  List<ServiceMetaDataProvider> serviceMetaDataProviders,
                                                  @Qualifier(SERVICE_CENTER_CLIENT_TASK_EXECUTOR) TaskScheduler taskScheduler) {
        NamingUtils.formatLog(log, "ServiceRegisterThread Started");
        return new ServiceRegisterThread(projectProperties, properties, eurekaClient, serviceMetaDataProviders, taskScheduler);
    }
    @Bean(name = SERVICE_CENTER_CLIENT_TASK_EXECUTOR)
    public TaskScheduler configWatchTaskScheduler() {
        var result = new ThreadPoolTaskScheduler();
        result.setPoolSize(1);
        return result;
    }
}