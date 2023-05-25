# 分布式任务管理


## 概念
协调者（coordinator）：采用集中式任务调度设计，主要完成任务配置管理、调度（运行/停止），运行状态跟踪等。
执行者（executors）：分布式任务执行者，主要完成任务的具体执行和结果反馈。

![image-20210404183752233](asserts/image-20210404183752233.png)



## 协调者
- 服务发现:

执行者会周期性自动注册自己持有的Job Handles，协调者会及时发现这样服务。

- 触发策略：

提供丰富的任务触发策略，包括：Cron触发、固定间隔触发、固定延时触发、API（事件）触发、人工触发、父子任务触发；

- 过期策略：

协调者错过调度时间的补偿处理策略，包括：忽略、立即补偿；

- 调度策略：

特定、轮询、随机、一致性HASH、最少调度、内存优先、CPU优先、响应性能优先和分片

- 分片策略：

一个任务可以让不同执行者执行时，可以选择分片策略：包括 All - 所有执行者都执行，One - 只有一个执行者执行，动态分片 - 条件过滤执行者执行；




## 执行者

- 服务注册：

执行者会周期性自动注册自己持有的Job Handles，协调者会及时发现这样服务。

- 阻塞策略：

调度过于密集执行器来不及处理时的处理策略，策略包括：单机串行（默认）、丢弃后续调度、覆盖之前调度；

- 异常处理：


超时  - 支持自定义任务超时时间，任务运行超时将会主动中断任务；

重试 - 支持自定义任务失败重试次数，当任务失败时将会按照预设的失败重试次数主动进行重试；

告警 - 默认提供邮件方式失败告警，同时预留扩展接口，可方便的扩展短信、钉钉等告警方式；

故障转移 - 任务路由策略选择”故障转移”情况下，如果执行器集群中某一台机器故障，将会自动Failover切换到一台正常的执行器发送调度请求。







# 定时、延时任务服务



## 内置任务类型

+ Sharding Job Handler：分片示例任务，任务内部模拟处理分片参数，可参考熟悉分片任务；
+ Http Job Handler：通用HTTP任务Handler；业务方只需要提供HTTP链接即可，不限制语言、平台；
+ Command Job Handler：通用命令行任务Handler；业务方只需要提供命令行即可；如 “pwd”命令；


## 项目中引用方法
1. pom 引用
```xml
 <dependency>
     <groupId>cn.zhumingwu</groupId>
     <artifactId>fast-job</artifactId>
     <version>${fast-job.version}</version>
 </dependency>
```

2.  配置JobConfig.java
```java
@Configuration
public class JobConfig {
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


> 3 创建执行器管理

<pre>

    import com.project.job.core.biz.model.Result;
    import com.project.job.core.handler.IJobHandler;
    import com.project.job.core.handler.annotation.JobHandler;
    import com.project.job.core.log.JobLogger;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Component;
    
    import java.util.concurrent.TimeUnit;
     
    @JobHandler(value="demoJobHandler")
    @Component
    @Slf4j
    public class CommandJobHandler extends IJobHandler {
    
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

</pre>


> 4 创建执行器管理
  + 在job-admin 中新增执行器 把自己创建的执行器 @JobHandler(value="demoJobHandler") 中 demoJobHandler 的名字添加进去
  +  AppName：对应 project.job.executor.appname
  +  名称   ：对应 demoJobHandler

> 5 添加任务管理
  + 在job-admin 中新增任务
  + 执行器： 对应执行器对应自己新建的 project.job.executor.appname 名字
  + JobHandler：对应自己创建的demoJobHandler
  + 配置Cron 

### 调度中心部署跟地址 [选填]：

### 如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；

project.job.admin.addresses=http://127.0.0.1:8080/-job-admin

### 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
project.job.executor.appname=-job-executor-sample

### 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
project.job.executor.ip=

### 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
project.job.executor.port=9999

### 执行器通讯TOKEN [选填]：非空时启用；
project.job.accessToken=

### 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
project.job.executor.logpath=/data/applogs/-job/jobhandler

### 执行器日志保存天数 [选填] ：值大于3时生效，启用执行器Log文件定期清理功能，否则不生效；
project.job.executor.logretentiondays=-1



