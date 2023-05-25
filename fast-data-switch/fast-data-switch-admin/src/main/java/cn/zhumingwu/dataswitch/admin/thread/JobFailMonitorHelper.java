package cn.zhumingwu.dataswitch.admin.thread;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobLogRepository;
import cn.zhumingwu.dataswitch.admin.enums.TriggerTypeEnum;
import lombok.var;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;
import cn.zhumingwu.dataswitch.admin.config.JobAdminConfig;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import cn.zhumingwu.dataswitch.admin.entity.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * job monitor instance
 */
public class JobFailMonitorHelper {
    // email alarm template
    private static final String mailBodyTemplate =
            "<h5>"
                    + I18nUtil.getInstance("").getString("jobconf_monitor_detail")
                    + "：</span>"
                    + "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n"
                    + "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >"
                    + "      <tr>\n"
                    + "         <td width=\"20%\" >"
                    + I18nUtil.getInstance("").getInstance("").getString("jobinfo_field_jobgroup")
                    + "</td>\n"
                    + "         <td width=\"10%\" >"
                    + I18nUtil.getInstance("").getInstance("").getString("jobinfo_field_id")
                    + "</td>\n"
                    + "         <td width=\"20%\" >"
                    + I18nUtil.getInstance("").getInstance("").getString("jobinfo_field_jobdesc")
                    + "</td>\n"
                    + "         <td width=\"10%\" >"
                    + I18nUtil.getInstance("").getInstance("").getString("jobconf_monitor_alarm_title")
                    + "</td>\n"
                    + "         <td width=\"40%\" >"
                    + I18nUtil.getInstance("").getString("jobconf_monitor_alarm_content")
                    + "</td>\n"
                    + "      </tr>\n"
                    + "   </thead>\n"
                    + "   <tbody>\n"
                    + "      <tr>\n"
                    + "         <td>{0}</td>\n"
                    + "         <td>{1}</td>\n"
                    + "         <td>{2}</td>\n"
                    + "         <td>"
                    + I18nUtil.getInstance("").getString("jobconf_monitor_alarm_type")
                    + "</td>\n"
                    + "         <td>{3}</td>\n"
                    + "      </tr>\n"
                    + "   </tbody>\n"
                    + "</table>";
    private static Logger logger = LoggerFactory.getLogger(JobFailMonitorHelper.class);
    private static JobFailMonitorHelper instance = new JobFailMonitorHelper();

    // ---------------------- monitor ----------------------
    private Thread monitorThread;
    private volatile boolean toStop = false;

    public static JobFailMonitorHelper getInstance() {
        return instance;
    }

    public void start() {
        monitorThread =
                new Thread(
                        new Runnable() {

                            @Override
                            public void run() {

                                // monitor
                                while (!toStop) {
                                    try {

                                        List<Integer> failLogIds = ApplicationContextHolder.getBean(JobLogRepository.class).findAll().stream().map(m -> m.getJobId()).collect(Collectors.toList());
                                        if (failLogIds != null && !failLogIds.isEmpty()) {
                                            for (var failLogId : failLogIds) {

//                                                // lock log
//                                                int lockRet =
//                                                        ApplicationContextHolder.getBean(JobLogService.class).update(  );
//                                                if (lockRet < 1) {
//                                                    continue;
//                                                }
                                                JobLog log = ApplicationContextHolder.getBean(JobLogRepository.class).findById(failLogId).get();
                                                JobInfo info =
                                                        ApplicationContextHolder.getBean(JobInfoRepository.class)
                                                                .findById(log.getJobId()).get();

                                                // 1、fail retry monitor
//                                                if (log.getExecutorFailRetryCount() > 0) {
//                                                    JobTriggerPoolHelper.trigger(
//                                                            log.getJobId().longValue(),
//                                                            TriggerTypeEnum.RETRY,
//                                                            (log.getExecutorFailRetryCount() - 1),
//                                                            log.getExecutorShardingParam(),
//                                                            log.getExecutorParam());
//                                                    String retryMsg =
//                                                            "<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>"
//                                                                    + I18nUtil.getInstance("").getString("jobconf_trigger_type_retry")
//                                                                    + "<<<<<<<<<<< </span><br>";
//                                                    log.setTriggerMsg(log.getTriggerMsg() + retryMsg);
//                                                    ApplicationContextHolder.getBean(JobLogRepository.class).save(log);
//                                                }

                                                // 2、fail alarm monitor
                                                int newAlarmStatus = 0; // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
                                                if (info != null
                                                        && info.getAlarmEmail() != null
                                                        && info.getAlarmEmail().trim().length() > 0) {
                                                    boolean alarmResult = true;
                                                    try {
                                                        alarmResult = failAlarm(info, log);
                                                    } catch (Exception e) {
                                                        alarmResult = false;
                                                        logger.error(e.getMessage(), e);
                                                    }
                                                    newAlarmStatus = alarmResult ? 2 : 3;
                                                } else {
                                                    newAlarmStatus = 1;
                                                }

//                                                ApplicationContextHolder.getBean(JobAdminConfig.class)
//                                                        .getJobLogDao()
//                                                        .updateAlarmStatus(failLogId, -1, newAlarmStatus);
                                            }
                                        }

                                    } catch (Exception e) {
                                        if (!toStop) {
                                        }
                                    }

                                    try {
                                        TimeUnit.SECONDS.sleep(10);
                                    } catch (Exception e) {
                                        if (!toStop) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }
                                }
                            }
                        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    // ---------------------- alarm ----------------------

    public void toStop() {
        toStop = true;
        // interrupt and wait
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * fail alarm
     *
     * @param jobLog
     */
    private boolean failAlarm(JobInfo info, JobLog jobLog) {
        boolean alarmResult = true;

        // send monitor email
        if (info != null && info.getAlarmEmail() != null && info.getAlarmEmail().trim().length() > 0) {

            // alarmContent
            String alarmContent = "Alarm Job LogId=" + jobLog.getId();
//            if (jobLog.getTriggerCode() != Result.success().getCode()) {
//                alarmContent += "<br>TriggerMsg=<br>" + jobLog.getTriggerMsg();
//            }
//            if (jobLog.getHandleCode() > 0 && jobLog.getHandleCode() != Result.success().getCode()) {
//                alarmContent += "<br>HandleCode=" + jobLog.getHandleMsg();
//            }

            // email info
            JobGroup group =
                    ApplicationContextHolder.getBean(JobGroupRepository.class)
                            .findById(info.getGroupId().intValue()).get();
            String personal = I18nUtil.getInstance("").getString("site_name_full");
            String title = I18nUtil.getInstance("").getString("jobconf_monitor");
            String content =
                    MessageFormat.format(
                            mailBodyTemplate,
                            group != null ? group.getTitle() : "null",
                            info.getId(),
                            info.getRemark(),
                            alarmContent);

            Set<String> emailSet = new HashSet<String>(Arrays.asList(info.getAlarmEmail().split(",")));
            for (String email : emailSet) {

                // make mail
                try {
                    MimeMessage mimeMessage =
                            ApplicationContextHolder.getBean(JavaMailSender.class).createMimeMessage();

                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//                    helper.setFrom(ApplicationContextHolder.getBean(JobAdminConfig.class).getEmailUserName(), personal);
                    helper.setTo(email);
                    helper.setSubject(title);
                    helper.setText(content, true);

                    ApplicationContextHolder.getBean(JavaMailSender.class).send(mimeMessage);
                } catch (Exception e) {

                    alarmResult = false;
                }
            }
        }

        // do something, custom alarm strategy, such as sms

        return alarmResult;
    }
}
