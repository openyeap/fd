# 分布式数据中心管理

## 概念
- 服务发现:

协调者通过服务中心获取所有提供任务处理者（JobHandler)。

- 服务注册：

执行者主动向服务中心注册服务，在MataData中以JobHandler作为主键，保存所有Handlers。


# 接口定义

## 执行者

- start(jobId, jobConfig) -> taskId 基于任务开始一个工作，返回工作的唯一编号
- stat(type,paramters) -> info 根据不同类型，查看统计信息，可以是任务日志可执行者的信息
- stop(taskId) -> bool 强制停止一个任务
- 

## 内置任务类型

+ Sharding Job Handler：分片示例任务，任务内部模拟处理分片参数，可参考熟悉分片任务；
+ Http Job Handler：通用HTTP任务Handler；业务方只需要提供HTTP链接即可，不限制语言、平台；
+ Command Job Handler：通用命令行任务Handler；业务方只需要提供命令行即可；如 “pwd”命令；


## 项目中引用方法
1. pom 引用
```xml
 <dependency>
     <groupId>cn.zhumingwu</groupId>
     <artifactId>fast-data-hub-core</artifactId>
     <version>${fast-job.version}</version>
 </dependency>
```

2.  配置DataHubConfig.java
```java
@Configuration
public class 配置DataHubConfig {
    private Logger logger = LoggerFactory.getLogger(JobConfig.class);

    @Value("${project.job.admin.addresses}")
    private String adminAddresses;
    
    @Value("${project.job.executor.appname}")
    private String appName;
    
    @Value("${project.job.executor.ip}")
    private String ip;
    
    @Value("${project.job.executor.port}")
    private int port;
    
    @Value("${project.job.accessToken}")
    private String accessToken;
    
    @Value("${project.job.executor.logpath}")
    private String logPath;
    
    @Value("${project.job.executor.logretentiondays}")
    private int logRetentionDays;
    
    @Bean(initMethod = "start", destroyMethod = "destroy")
    public JobSpringExecutor JobExecutor() {
        logger.info("job config init.");
        JobSpringExecutor JobSpringExecutor = new JobSpringExecutor();
        JobSpringExecutor.setAdminAddresses(adminAddresses);
        JobSpringExecutor.setAppName(appName);
        JobSpringExecutor.setIp(ip);
        JobSpringExecutor.setPort(port);
        JobSpringExecutor.setAccessToken(accessToken);
        JobSpringExecutor.setLogPath(logPath);
        JobSpringExecutor.setLogRetentionDays(logRetentionDays);
    
        return JobSpringExecutor;
    }
}
```

3. 创建执行器管理

```
import com.project.job.core.biz.model.Result;
import com.project.job.core.handler.IJobHandler;
import com.project.job.core.handler.annotation.JobHandler;
import com.project.job.core.log.JobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
 
@Job(value="demoJobHandler")
@Component
@Slf4j
public class CommandJobHandler extends JobHandler {

    @Override
    public Result<String> execute(String param) throws Exception {
        JobLogger.log("-JOB, Hello World.");

        for (int i = 0; i < 5; i++) {
            JobLogger.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }

        log.info("hello world!");

        return SUCCESS;
    }

}
```

