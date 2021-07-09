package ltd.fdsa.job.admin.config;

import ltd.fdsa.job.admin.scheduler.JobScheduler;
import ltd.fdsa.job.admin.jpa.service.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Component
public class JobAdminConfig implements InitializingBean, DisposableBean {


    private JobScheduler JobScheduler;

    // ---------------------- JobScheduler ----------------------
    // conf
    @Value("${project.job.i18n}")
    private String i18n;
    @Value("${project.job.accessToken}")
    private String accessToken;
    @Value("${spring.mail.username}")
    private String emailUserName;


    @Resource
    private JobLogService JobLogDao;
    @Resource
    private JobInfoService JobInfoDao;
    @Resource
    private JobRegistryService JobRegistryDao;

    // dao, service
    @Resource
    private JobGroupService JobGroupDao;
    @Resource
    private JobLogReportService  JobLogReportDao;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private DataSource dataSource;


    @Override
    public void afterPropertiesSet() throws Exception {

        JobScheduler = new JobScheduler();
        JobScheduler.init();
    }

    @Override
    public void destroy() throws Exception {
        JobScheduler.destroy();
    }

    public String getI18n() {
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmailUserName() {
        return emailUserName;
    }

    public JobLogService getJobLogDao() {
        return JobLogDao;
    }

    public JobInfoService getJobInfoDao() {
        return JobInfoDao;
    }

    public JobRegistryService getJobRegistryDao() {
        return JobRegistryDao;
    }

    public JobGroupService getJobGroupDao() {
        return JobGroupDao;
    }

    public JobLogReportService getJobLogReportDao() {
        return JobLogReportDao;
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
