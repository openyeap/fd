package cn.zhumingwu.dataswitch.client.config;

import cn.zhumingwu.dataswitch.client.utils.HessianProxyFactoryUtil;
import cn.zhumingwu.dataswitch.core.container.PluginManager;
import cn.zhumingwu.dataswitch.core.job.coordinator.Coordinator;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.dataswitch.core.job.executor.ExecutorImpl;
import cn.zhumingwu.dataswitch.core.job.executor.JobExecutor;
import cn.zhumingwu.dataswitch.core.job.executor.SpringJobExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.scheduling.TaskScheduler;

import java.util.Properties;

@Configuration
@Slf4j
public class ExecutorConfig {
//    @Bean("/executor")
//    public HessianServiceExporter exportExecutorHessian() {
//        ExecutorImpl executor = new ExecutorImpl();
//        HessianServiceExporter exporter = new HessianServiceExporter();
//        exporter.setService(executor);
//        exporter.setServiceInterface(Executor.class);
//        return exporter;
//    }

    @Bean
    public Executor executor() {
        return new ExecutorImpl();
    }

    @Bean
    public JobExecutor jobExecutor(Properties properties) {
        return new SpringJobExecutor(properties);
    }

    @Bean
    public Coordinator coordinatorClient() {
        try {
            return HessianProxyFactoryUtil.getHessianClientBean(Coordinator.class, "url");
        } catch (Exception e) {
            return null;
        }
    }
}
