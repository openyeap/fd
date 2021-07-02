package ltd.fdsa.switcher.core.job.executor;

import ltd.fdsa.switcher.core.job.handler.impl.CommandJobHandler;
import ltd.fdsa.switcher.core.job.handler.impl.HttpJobHandler;
import ltd.fdsa.switcher.core.job.handler.impl.ShardingJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class FrameLessJobConfig {
    private static Logger logger = LoggerFactory.getLogger(FrameLessJobConfig.class);

    private static FrameLessJobConfig instance = new FrameLessJobConfig();
    private JobExecutor JobExecutor = null;

    public static FrameLessJobConfig getInstance() {
        return instance;
    }

    public static Properties loadProperties(String propertyFileName) {
        InputStreamReader in = null;
        try {
            ClassLoader loder = Thread.currentThread().getContextClassLoader();

            in = new InputStreamReader(loder.getResourceAsStream(propertyFileName), "UTF-8");
            ;
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

    /**
     * init
     */
    public void initJobExecutor() {

        // registry jobhandler
        JobExecutor.registerJobHandler("shardingJobHandler", new ShardingJobHandler());
        JobExecutor.registerJobHandler("httpJobHandler", new HttpJobHandler());
        JobExecutor.registerJobHandler("commandJobHandler", new CommandJobHandler());

        // load executor prop
        Properties JobProp = loadProperties("job-executor.properties");

        // init executor
        JobExecutor = new JobExecutor(JobProp);
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
}
