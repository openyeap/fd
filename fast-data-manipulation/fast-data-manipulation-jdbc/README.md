# 定时、延时任务服务

### 系统采用 xxl-job 集成具体参考 http://www.xuxueli.com/



####内置任务类型
+ demoJobHandler：简单示例任务，任务内部模拟耗时任务逻辑，用户可在线体验Rolling Log等功能；
+ shardingJobHandler：分片示例任务，任务内部模拟处理分片参数，可参考熟悉分片任务；
+ httpJobHandler：通用HTTP任务Handler；业务方只需要提供HTTP链接即可，不限制语言、平台；
+ commandJobHandler：通用命令行任务Handler；业务方只需要提供命令行即可；如 “pwd”命令；


### 使用方法
#### 作为服务启动 java -jar mti-service-job-0.0.1-SNAPSHOT-exec.jar
      访问 http://localhost:30003 任务调度管理中心

#### 项目中引用方法
> 1.pom 引用
<pre>
    &lt;dependency&gt;
        &lt;groupId&gt;com.mti&lt;/groupId&gt;
        &lt;artifactId&gt;mti-service-job&lt;/artifactId&gt;
        &lt;version&gt;${mti.version}&lt;/version&gt;
    &lt;/dependency&gt;
</pre>
> 2.配置 XxlJobConfig.java
 <pre>

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class XxlJobConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.executor.appname}")
    private String appName;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppName(appName);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     *
     *      1、引入依赖：
     *          <dependency>
     *             <groupId>org.springframework.cloud</groupId>
     *             <artifactId>spring-cloud-commons</artifactId>
     *             <version>${version}</version>
     *         </dependency>
     *
     *      2、配置文件，或者容器启动变量
     *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     *
     *      3、获取IP
     *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */


}

 
 </pre>


> 3 创建执行器管理

<pre>
    
    import com.xxl.job.core.biz.model.ReturnT;
    import com.xxl.job.core.handler.IJobHandler;
    import com.xxl.job.core.handler.annotation.JobHandler;
    import com.xxl.job.core.log.XxlJobLogger;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Component;
    
    import java.util.concurrent.TimeUnit;
    
    /**
     * @ClassName: CommandJobHandler
     * @description:
     * @author: Allen
     * @create: 2019-07-11 09:45
     **/
    @JobHandler(value="demoJobHandler")
    @Component
    @Slf4j
    public class CommandJobHandler extends IJobHandler {
    
        @Override
        public ReturnT<String> execute(String param) throws Exception {
            XxlJobLogger.log("XXL-JOB, Hello World.");
    
            for (int i = 0; i < 5; i++) {
                XxlJobLogger.log("beat at:" + i);
                TimeUnit.SECONDS.sleep(2);
            }
    
            log.info("hello world!");
    
            return SUCCESS;
        }
    
    }

</pre>


> 4 创建执行器管理
  + 在job-admin 中新增执行器 把自己创建的执行器 @JobHandler(value="demoJobHandler") 中 demoJobHandler 的名字添加进去
  +  AppName：对应 xxl.job.executor.appname
  +  名称   ：对应 demoJobHandler

> 5 添加任务管理
  + 在job-admin 中新增任务
  + 执行器： 对应执行器对应自己新建的 xxl.job.executor.appname 名字
  + JobHandler：对应自己创建的demoJobHandler
  + 配置Cron 


### 调度中心部署跟地址 [选填]：如调度中心集群部署存在多个地址则用逗号分隔。执行器将会使用该地址进行"执行器心跳注册"和"任务结果回调"；为空则关闭自动注册；
xxl.job.admin.addresses=http://127.0.0.1:8080/xxl-job-admin

### 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
xxl.job.executor.appname=xxl-job-executor-sample

### 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
xxl.job.executor.ip=

### 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
xxl.job.executor.port=9999

### 执行器通讯TOKEN [选填]：非空时启用；
xxl.job.accessToken=

### 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
xxl.job.executor.logpath=/data/applogs/xxl-job/jobhandler

### 执行器日志保存天数 [选填] ：值大于3时生效，启用执行器Log文件定期清理功能，否则不生效；
xxl.job.executor.logretentiondays=-1

