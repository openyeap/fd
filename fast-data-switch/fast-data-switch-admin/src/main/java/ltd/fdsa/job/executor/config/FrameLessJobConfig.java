package ltd.fdsa.job.executor.config;
 

import ltd.fdsa.job.core.executor.JobExecutor;
import ltd.fdsa.job.executor.jobhandler.CommandJobHandler;
import ltd.fdsa.job.executor.jobhandler.DemoJobHandler;
import ltd.fdsa.job.executor.jobhandler.HttpJobHandler;
import ltd.fdsa.job.executor.jobhandler.ShardingJobHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class FrameLessJobConfig {
    private static Logger logger = LoggerFactory.getLogger(FrameLessJobConfig.class);


    private static FrameLessJobConfig instance = new FrameLessJobConfig();
    public static FrameLessJobConfig getInstance() {
        return instance;
    }


    private JobExecutor JobExecutor = null;

    /**
     * init
     */
    public void initJobExecutor() {

        // registry jobhandler
        JobExecutor.registJobHandler("demoJobHandler", new DemoJobHandler());
        JobExecutor.registJobHandler("shardingJobHandler", new ShardingJobHandler());
        JobExecutor.registJobHandler("httpJobHandler", new HttpJobHandler());
        JobExecutor.registJobHandler("commandJobHandler", new CommandJobHandler());

        // load executor prop
        Properties JobProp = loadProperties("-executor.properties");


        // init executor
        JobExecutor = new JobExecutor();
        JobExecutor.setAdminAddresses(JobProp.getProperty("xxl.job.admin.addresses"));
        JobExecutor.setAppName(JobProp.getProperty("xxl.job.executor.appname"));
        JobExecutor.setIp(JobProp.getProperty("xxl.job.executor.ip"));
        JobExecutor.setPort(Integer.valueOf(JobProp.getProperty("xxl.job.executor.port")));
        JobExecutor.setAccessToken(JobProp.getProperty("xxl.job.accessToken"));
        JobExecutor.setLogPath(JobProp.getProperty("xxl.job.executor.logpath"));
        JobExecutor.setLogRetentionDays(Integer.valueOf(JobProp.getProperty("xxl.job.executor.logretentiondays")));

        // start executor
        try {
            JobExecutor.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * destory
     */
    public void destoryJobExecutor() {
        if (JobExecutor != null) {
            JobExecutor.destroy();
        }
    }


    public static Properties loadProperties(String propertyFileName) {
        InputStreamReader in = null;
        try {
            ClassLoader loder = Thread.currentThread().getContextClassLoader();

            in = new InputStreamReader(loder.getResourceAsStream(propertyFileName), "UTF-8");;
            if (in != null) {
                Properties prop = new Properties();
                prop.load(in);
                return prop;
            }
        } catch (IOException e) {
            logger.error("load {} error!", propertyFileName);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("close {} error!", propertyFileName);
                }
            }
        }
        return null;
    }

}
