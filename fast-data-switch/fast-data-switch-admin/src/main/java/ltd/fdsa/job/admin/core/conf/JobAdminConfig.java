package ltd.fdsa.job.admin.core.conf;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import ltd.fdsa.job.admin.core.scheduler.JobScheduler;
import ltd.fdsa.job.admin.dao.*;

import javax.annotation.Resource;
import javax.sql.DataSource;



@Component
public class JobAdminConfig implements InitializingBean, DisposableBean {

    private static JobAdminConfig adminConfig = null;
    public static JobAdminConfig getAdminConfig() {
        return adminConfig;
    }


    // ---------------------- JobScheduler ----------------------

    private JobScheduler JobScheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;

        JobScheduler = new JobScheduler();
        JobScheduler.init();
    }

    @Override
    public void destroy() throws Exception {
        JobScheduler.destroy();
    }


    // ---------------------- JobScheduler ----------------------

    // conf
    @Value("${xxl.job.i18n}")
    private String i18n;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${spring.mail.username}")
    private String emailUserName;

    @Value("${xxl.job.triggerpool.fast.max}")
    private int triggerPoolFastMax;

    @Value("${xxl.job.triggerpool.slow.max}")
    private int triggerPoolSlowMax;

    @Value("${xxl.job.logretentiondays}")
    private int logretentiondays;

    // dao, service

    @Resource
    private JobLogDao JobLogDao;
    @Resource
    private JobInfoDao JobInfoDao;
    @Resource
    private JobRegistryDao JobRegistryDao;
    @Resource
    private JobGroupDao JobGroupDao;
    @Resource
    private JobLogReportDao JobLogReportDao;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private DataSource dataSource;


    public String getI18n() {
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public int getTriggerPoolFastMax() {
        if (triggerPoolFastMax < 200) {
            return 200;
        }
        return triggerPoolFastMax;
    }

    public int getTriggerPoolSlowMax() {
        if (triggerPoolSlowMax < 100) {
            return 100;
        }
        return triggerPoolSlowMax;
    }

    public int getLogretentiondays() {
        if (logretentiondays < 7) {
            return -1;  // Limit greater than or equal to 7, otherwise close
        }
        return logretentiondays;
    }

    public JobLogDao getJobLogDao() {
        return JobLogDao;
    }

    public JobInfoDao getJobInfoDao() {
        return JobInfoDao;
    }

    public JobRegistryDao getJobRegistryDao() {
        return JobRegistryDao;
    }

    public JobGroupDao getJobGroupDao() {
        return JobGroupDao;
    }

    public JobLogReportDao getJobLogReportDao() {
        return JobLogReportDao;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
