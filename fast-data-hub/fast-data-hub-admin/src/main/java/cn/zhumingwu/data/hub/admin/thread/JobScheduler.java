package cn.zhumingwu.data.hub.admin.thread;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.data.hub.admin.entity.JobInfo;
import cn.zhumingwu.data.hub.admin.enums.TriggerType;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.core.job.cron.CronExpression;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.context.SmartLifecycle;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class JobScheduler implements SmartLifecycle {


    public static final long PRE_READ_MS = 5000; // 预读 5s 内的JobInfo
    private final RingData ringData = new RingData();

    private final Thread scheduleThread; // 定期读 JobInfo
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Thread ringThread;  //每秒触发

    public JobScheduler(CoordinatorContext context) {
        // schedule thread
        var jobReader = new JobReader(context);
        scheduleThread = new Thread(jobReader);
        // ring thread
        var jobRingTrigger = new JobRingTrigger(context);
        ringThread = new Thread(jobRingTrigger);
    }

    public void start() {
        scheduleThread.setDaemon(true);
        scheduleThread.start();
        ringThread.setDaemon(true);
        ringThread.start();
    }

    @Override
    public void stop() {
        // 1、stop schedule
        if (this.running.compareAndSet(true, false)) {

            try {
                TimeUnit.SECONDS.sleep(1); // wait
            } catch (InterruptedException e) {
                log.error("error", e);
            }
            if (scheduleThread.getState() != Thread.State.TERMINATED) {
                // interrupt and wait
                scheduleThread.interrupt();
                try {
                    scheduleThread.join();
                } catch (InterruptedException e) {
                    log.error("error", e);
                }
            }

            // if has ring data
            boolean hasRingData = false;

            for (var second : ringData.getAll()) {
                var tmpData = second;
                if (tmpData != null && tmpData.size() > 0) {
                    hasRingData = true;
                    break;
                }
            }
            if (hasRingData) {
                try {
                    TimeUnit.SECONDS.sleep(8);
                } catch (InterruptedException e) {
                    log.error("error", e);
                }
            }

            // stop ring (wait job-in-memory stop)

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error("error", e);
            }
            if (ringThread.getState() != Thread.State.TERMINATED) {
                // interrupt and wait
                ringThread.interrupt();
                try {
                    ringThread.join();
                } catch (InterruptedException e) {
                    log.error("error", e);
                }
            }
        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    private void refreshNextValidTime(JobInfo jobInfo, Date fromTime) throws ParseException {
        Date nextValidTime = new CronExpression(jobInfo.getScheduleExpression()).getNextValidTimeAfter(fromTime);
        if (nextValidTime != null) {
            jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
            jobInfo.setTriggerNextTime(nextValidTime.getTime());
        } else {
            jobInfo.setTriggerStatus(0);
            jobInfo.setTriggerLastTime(0);
            jobInfo.setTriggerNextTime(0);
        }
    }

    private void pushTimeRing(Long ringSecond, Long jobId) {
        ringData.getItem(ringSecond).add(jobId);
    }


    private class JobRingTrigger implements Runnable {

        private final CoordinatorContext context;

        private JobRingTrigger(CoordinatorContext context) {
            this.context = context;
        }

        @Override
        public void run() {

            // align second
            try {
                TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                log.error("error", e);
            }

            while (running.get()) {

                try {
                    // second data
                    List<Long> ringItemData = new ArrayList<>();
                    // 避免处理耗时太长，跨过刻度，向前校验一个刻度；
                    int nowSecond = Calendar.getInstance().get(Calendar.SECOND);
                    for (int i = 0; i < 2; i++) {
                        List<Long> tmpData = ringData.fetchItem(nowSecond);
                        if (tmpData != null) {
                            ringItemData.addAll(tmpData);
                        }
                    }

                    // ring trigger
                    if (ringItemData.size() > 0) {
                        // do trigger
                        for (Long jobId : ringItemData) {
                            // do trigger
                            this.context.trigger(jobId, TriggerType.CRON, null, null, 0);
                        }
                        // clear
                        ringItemData.clear();
                    }
                } catch (Exception e) {
                    log.error("error", e);

                }

                // next second, align second
                try {
                    TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                } catch (InterruptedException e) {
                    log.error("error", e);
                }
            }

        }
    }

    private class JobReader implements Runnable {
        private final CoordinatorContext context;

        public JobReader(CoordinatorContext context) {
            this.context = context;
        }

        @Override
        public void run() {
            // 时间对齐
            try {
                TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                log.error("error", e);
            }

            // pre-read count: thread pool - size * trigger-qps (each trigger cost 50ms, qps = 1000/50 = 20)
            int preReadCount = (10 + 20) * 20;

            while (running.get()) {

                // Scan Job
                long start = System.currentTimeMillis();
                Connection conn = null;
                Boolean connAutoCommit = null;
                PreparedStatement preparedStatement = null;

                boolean preReadSuc = true;
                try {

                    conn = ApplicationContextHolder.getBean(DataSource.class).getConnection();
                    connAutoCommit = conn.getAutoCommit();
                    conn.setAutoCommit(false);
                    preparedStatement = conn.prepareStatement("select * from t_job_lock where lock_name = 'schedule_lock' for update");
                    preparedStatement.execute();
                    // tx start
                    // 1、pre read
                    long nowTime = System.currentTimeMillis();
                    List<JobInfo> scheduleList = ApplicationContextHolder.getBean(JobInfoRepository.class).findAll();
                    if (scheduleList.size() > 0) {
                        // 2、push time-ring
                        for (JobInfo jobInfo : scheduleList) {

                            // time-ring jump
                            if (nowTime > jobInfo.getTriggerNextTime() + PRE_READ_MS) {
                                // 2.1、trigger-expire > 5s：pass && make next-trigger-time
                                // fresh next
                                refreshNextValidTime(jobInfo, new Date());
                            } else if (nowTime > jobInfo.getTriggerNextTime()) {
                                // 2.2、trigger-expire < 5s：direct-trigger && make next-trigger-time
                                // 1、trigger
                                this.context.trigger(jobInfo, TriggerType.CRON);
                                // 2、fresh next
                                refreshNextValidTime(jobInfo, new Date());
                                // next-trigger-time in 5s, pre-read again
                                if (jobInfo.getTriggerStatus() == 1 && nowTime + PRE_READ_MS > jobInfo.getTriggerNextTime()) {

                                    // 1、make ring second
                                    Long ringSecond = (Long) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                                    // 2、push time ring
                                    pushTimeRing(ringSecond, jobInfo.getId());

                                    // 3、fresh next
                                    refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));
                                }

                            } else {
                                // 2.3、trigger-pre-read：time-ring trigger && make next-trigger-time

                                // 1、make ring second
                                Long ringSecond = (Long) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                                // 2、push time ring
                                pushTimeRing(ringSecond, jobInfo.getId());

                                // 3、fresh next
                                refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));
                            }
                        }

                        // 3、update trigger info
                        for (JobInfo jobInfo : scheduleList) {
                            ApplicationContextHolder.getBean(JobInfoRepository.class).save(jobInfo);
                        }

                    } else {
                        preReadSuc = false;
                    }

                    // tx stop

                } catch (Exception e) {
                    log.error("error", e);
                } finally {

                    // commit
                    if (conn != null) {
                        try {
                            conn.commit();
                        } catch (SQLException e) {
                            log.error("error", e);
                        }
                        try {
                            conn.setAutoCommit(connAutoCommit);
                        } catch (SQLException e) {
                            log.error("error", e);

                        }
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            log.error("error", e);

                        }
                    }

                    // close PreparedStatement
                    if (null != preparedStatement) {
                        try {
                            preparedStatement.close();
                        } catch (SQLException e) {
                            log.error("error", e);

                        }
                    }
                }
                long cost = System.currentTimeMillis() - start;

                // Wait seconds, align second
                if (cost < 1000) { // scan-overtime, not wait
                    try {
                        // pre-read period: success > scan each second; fail > skip this period;
                        TimeUnit.MILLISECONDS.sleep(
                                (preReadSuc ? 1000 : PRE_READ_MS) - System.currentTimeMillis() % 1000);
                    } catch (InterruptedException e) {
                        log.error("error", e);

                    }
                }
            }
        }
    }
}
