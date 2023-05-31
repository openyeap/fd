package cn.zhumingwu.dataswitch.admin.thread;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.admin.entity.JobTask;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobTaskRepository;
import cn.zhumingwu.dataswitch.core.job.enums.JobStatus;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;
import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.domain.Example;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * job fail monitor 定期扫描数据库执行失败的任务
 */
@Slf4j
public class JobFailMonitor implements SmartLifecycle {

    private final Thread monitorThread;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public JobFailMonitor() {
        var runnable = new JobFailMonitorRunnable();
        this.monitorThread = new Thread(runnable);
    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            monitorThread.setDaemon(true);
            monitorThread.start();
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            this.monitorThread.interrupt();
            try {
                monitorThread.join();
            } catch (InterruptedException e) {
                log.error("error", e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }


    /**
     * JobFailTask
     */

    class JobFailMonitorRunnable implements Runnable {

        private final String mailBodyTemplate =
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

        private Result<Boolean> failAlarm(JobInfo info, JobTask jobLog) {
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


                String personal = I18nUtil.getInstance("").getString("site_name_full");
                String title = I18nUtil.getInstance("").getString("jobconf_monitor");
                String content = MessageFormat.format(mailBodyTemplate, info.getId(), info.getRemark(), alarmContent);
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
            if (alarmResult) {
                return Result.success(alarmResult);
            }
            return Result.fail(HttpCode.INTERNAL_SERVER_ERROR);
        }

        @Override
        public void run() {
            while (running.get()) {
                try {
                    var query = new JobTask();
                    query.setHandleStatus(JobStatus.FAILED);
                    Example<JobTask> example = Example.of(query);
                    for (var jobTask : ApplicationContextHolder.getBean(JobTaskRepository.class).findAll(example)) {
                        var optionalJobInfo = ApplicationContextHolder.getBean(JobInfoRepository.class).findById(jobTask.getJobId());
                        if (!optionalJobInfo.isPresent()) {
                            continue;
                        }
                        JobInfo info = optionalJobInfo.get();

                        // 1、fail retry monitor
                        if (jobTask.getExecutorFailRetryCount() < info.getExecutorRetryTimes()) {
//                            JobTriggerPoolHelper.trigger(
//                                    log.getJobId().longValue(),
//                                    TriggerTypeEnum.RETRY,
//                                    (log.getExecutorFailRetryCount() - 1),
//                                    log.getExecutorShardingParam(),
//                                    log.getExecutorParam());


                            jobTask.setExecutorFailRetryCount(jobTask.getExecutorFailRetryCount() + 1);
                        }

                        // 2、fail alarm monitor

                        if (!Strings.isNullOrEmpty(info.getAlarmEmail())) {
                            Result<Boolean> alarmResult = null;
                            try {
                                alarmResult = failAlarm(info, jobTask);
                            } catch (Exception e) {
                                alarmResult = Result.error(e, HttpCode.INTERNAL_SERVER_ERROR.getCode());
                                log.error("error", e);
                            }
                            jobTask.setAlarmStatus(alarmResult.getCode());
                        }

                    }

                } catch (Exception e) {
                    log.error("error", e);
                }

                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    log.error("error", e);
                }
            }
        }
    }
}
