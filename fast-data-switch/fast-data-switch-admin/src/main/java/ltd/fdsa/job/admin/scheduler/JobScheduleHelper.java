package ltd.fdsa.job.admin.scheduler;

import ltd.fdsa.core.context.ApplicationContextHolder;
import ltd.fdsa.job.admin.thread.JobTriggerPoolHelper;
import ltd.fdsa.job.admin.trigger.TriggerTypeEnum;
import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import ltd.fdsa.job.admin.jpa.service.JobInfoService;
import ltd.fdsa.switcher.core.job.cron.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class JobScheduleHelper {
    public static final long PRE_READ_MS = 5000; // pre read
    private static Logger logger = LoggerFactory.getLogger(JobScheduleHelper.class);
    private static JobScheduleHelper instance = new JobScheduleHelper();
    private static volatile Map<Integer, List<Integer>> ringData = new ConcurrentHashMap<>();
    private Thread scheduleThread;
    private Thread ringThread;
    private volatile boolean scheduleThreadToStop = false;
    private volatile boolean ringThreadToStop = false;

    public static JobScheduleHelper getInstance() {
        return instance;
    }

    public void start() {

        // schedule thread
        scheduleThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis() % 1000);
                                } catch (InterruptedException e) {
                                    if (!scheduleThreadToStop) {
                                        logger.error(e.getMessage(), e);
                                    }
                                }

                                // pre-read count: treadpool-size * trigger-qps (each trigger cost 50ms, qps =
                                // 1000/50 = 20)
                                int preReadCount = (10 + 20) * 20;

                                while (!scheduleThreadToStop) {

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
                                        List<JobInfo> scheduleList = ApplicationContextHolder.getBean(JobInfoService.class).findAll();
                                        if (scheduleList != null && scheduleList.size() > 0) {
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
                                                    JobTriggerPoolHelper.trigger(
                                                            jobInfo.getId(), TriggerTypeEnum.CRON, -1, null, null);

                                                    // 2、fresh next
                                                    refreshNextValidTime(jobInfo, new Date());

                                                    // next-trigger-time in 5s, pre-read again
                                                    if (jobInfo.getTriggerStatus() == 1
                                                            && nowTime + PRE_READ_MS > jobInfo.getTriggerNextTime()) {

                                                        // 1、make ring second
                                                        int ringSecond = (int) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                                                        // 2、push time ring
                                                        pushTimeRing(ringSecond, jobInfo.getId());

                                                        // 3、fresh next
                                                        refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));
                                                    }

                                                } else {
                                                    // 2.3、trigger-pre-read：time-ring trigger && make next-trigger-time

                                                    // 1、make ring second
                                                    int ringSecond = (int) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                                                    // 2、push time ring
                                                    pushTimeRing(ringSecond, jobInfo.getId());

                                                    // 3、fresh next
                                                    refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));
                                                }
                                            }

                                            // 3、update trigger info
                                            for (JobInfo jobInfo : scheduleList) {
                                                ApplicationContextHolder.getBean(JobInfoService.class).update(jobInfo);
                                            }

                                        } else {
                                            preReadSuc = false;
                                        }

                                        // tx stop

                                    } catch (Exception e) {
                                        if (!scheduleThreadToStop) {
                                        }
                                    } finally {

                                        // commit
                                        if (conn != null) {
                                            try {
                                                conn.commit();
                                            } catch (SQLException e) {
                                                if (!scheduleThreadToStop) {
                                                    logger.error(e.getMessage(), e);
                                                }
                                            }
                                            try {
                                                conn.setAutoCommit(connAutoCommit);
                                            } catch (SQLException e) {
                                                if (!scheduleThreadToStop) {
                                                    logger.error(e.getMessage(), e);
                                                }
                                            }
                                            try {
                                                conn.close();
                                            } catch (SQLException e) {
                                                if (!scheduleThreadToStop) {
                                                    logger.error(e.getMessage(), e);
                                                }
                                            }
                                        }

                                        // close PreparedStatement
                                        if (null != preparedStatement) {
                                            try {
                                                preparedStatement.close();
                                            } catch (SQLException e) {
                                                if (!scheduleThreadToStop) {
                                                    logger.error(e.getMessage(), e);
                                                }
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
                                            if (!scheduleThreadToStop) {
                                                logger.error(e.getMessage(), e);
                                            }
                                        }
                                    }
                                }
                            }
                        });
        scheduleThread.setDaemon(true);
        scheduleThread.start();

        // ring thread
        ringThread =
                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {

                                // align second
                                try {
                                    TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                                } catch (InterruptedException e) {
                                    if (!ringThreadToStop) {
                                        logger.error(e.getMessage(), e);
                                    }
                                }

                                while (!ringThreadToStop) {

                                    try {
                                        // second data
                                        List<Integer> ringItemData = new ArrayList<>();
                                        int nowSecond =
                                                Calendar.getInstance().get(Calendar.SECOND); // 避免处理耗时太长，跨过刻度，向前校验一个刻度；
                                        for (int i = 0; i < 2; i++) {
                                            List<Integer> tmpData = ringData.remove((nowSecond + 60 - i) % 60);
                                            if (tmpData != null) {
                                                ringItemData.addAll(tmpData);
                                            }
                                        }

                                        // ring trigger
                                        if (ringItemData.size() > 0) {
                                            // do trigger
                                            for (int jobId : ringItemData) {
                                                // do trigger
                                                JobTriggerPoolHelper.trigger(jobId, TriggerTypeEnum.CRON, -1, null, null);
                                            }
                                            // clear
                                            ringItemData.clear();
                                        }
                                    } catch (Exception e) {
                                        if (!ringThreadToStop) {
                                        }
                                    }

                                    // next second, align second
                                    try {
                                        TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                                    } catch (InterruptedException e) {
                                        if (!ringThreadToStop) {
                                            logger.error(e.getMessage(), e);
                                        }
                                    }
                                }
                            }
                        });
        ringThread.setDaemon(true);
        ringThread.start();
    }

    private void refreshNextValidTime(JobInfo jobInfo, Date fromTime) throws ParseException {
        Date nextValidTime = new CronExpression(jobInfo.getCronExpression()).getNextValidTimeAfter(fromTime);
        if (nextValidTime != null) {
            jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
            jobInfo.setTriggerNextTime(nextValidTime.getTime());
        } else {
            jobInfo.setTriggerStatus(0);
            jobInfo.setTriggerLastTime(0);
            jobInfo.setTriggerNextTime(0);
        }
    }

    private void pushTimeRing(int ringSecond, int jobId) {
        // push async ring
        List<Integer> ringItemData = ringData.get(ringSecond);
        if (ringItemData == null) {
            ringItemData = new ArrayList<Integer>();
            ringData.put(ringSecond, ringItemData);
        }
        ringItemData.add(jobId);
    }

    public void toStop() {

        // 1、stop schedule
        scheduleThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1); // wait
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        if (scheduleThread.getState() != Thread.State.TERMINATED) {
            // interrupt and wait
            scheduleThread.interrupt();
            try {
                scheduleThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // if has ring data
        boolean hasRingData = false;
        if (!ringData.isEmpty()) {
            for (int second : ringData.keySet()) {
                List<Integer> tmpData = ringData.get(second);
                if (tmpData != null && tmpData.size() > 0) {
                    hasRingData = true;
                    break;
                }
            }
        }
        if (hasRingData) {
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // stop ring (wait job-in-memory stop)
        ringThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        if (ringThread.getState() != Thread.State.TERMINATED) {
            // interrupt and wait
            ringThread.interrupt();
            try {
                ringThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
