package cn.zhumingwu.dataswitch.admin.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * job log report helper
 */
@Slf4j
public class JobLogReportHelper {


    private static JobLogReportHelper instance = new JobLogReportHelper();
    private Thread logrThread;
    private volatile boolean toStop = false;

    public static JobLogReportHelper getInstance() {
        return instance;
    }

    public void start() {
        logrThread =
                new Thread(
                        new Runnable() {

                            @Override
                            public void run() {

                                // last clean log time
                                long lastCleanLogTime = 0;

                                while (!toStop) {

                                    // 1、log-report refresh: refresh log report in 3 days
                                    try {

                                        for (int i = 0; i < 3; i++) {

                                            // today
                                            Calendar itemDay = Calendar.getInstance();
                                            itemDay.add(Calendar.DAY_OF_MONTH, -i);
                                            itemDay.set(Calendar.HOUR_OF_DAY, 0);
                                            itemDay.set(Calendar.MINUTE, 0);
                                            itemDay.set(Calendar.SECOND, 0);
                                            itemDay.set(Calendar.MILLISECOND, 0);

                                            Date todayFrom = itemDay.getTime();

                                            itemDay.set(Calendar.HOUR_OF_DAY, 23);
                                            itemDay.set(Calendar.MINUTE, 59);
                                            itemDay.set(Calendar.SECOND, 59);
                                            itemDay.set(Calendar.MILLISECOND, 999);

                                            Date todayTo = itemDay.getTime();

                                            // refresh log-report every minute


//                                            Map<String, Object> triggerCountMap =
//                                                    ApplicationContextHolder.getBean(JobAdminConfig.class)
//                                                            .getJobLogDao()
//                                                            .findLogReport(todayFrom, todayTo);
//                                            if (triggerCountMap != null && triggerCountMap.size() > 0) {
//                                                int triggerDayCount =
//                                                        triggerCountMap.containsKey("triggerDayCount")
//                                                                ? Integer.valueOf(
//                                                                String.valueOf(triggerCountMap.get("triggerDayCount")))
//                                                                : 0;
//                                                int triggerDayCountRunning =
//                                                        triggerCountMap.containsKey("triggerDayCountRunning")
//                                                                ? Integer.valueOf(
//                                                                String.valueOf(triggerCountMap.get("triggerDayCountRunning")))
//                                                                : 0;
//                                                int triggerDayCountSuc =
//                                                        triggerCountMap.containsKey("triggerDayCountSuc")
//                                                                ? Integer.valueOf(
//                                                                String.valueOf(triggerCountMap.get("triggerDayCountSuc")))
//                                                                : 0;
//                                                int triggerDayCountFail =
//                                                        triggerDayCount - triggerDayCountRunning - triggerDayCountSuc;
//
//                                                JobLogReport.setRunningCount(triggerDayCountRunning);
//                                                JobLogReport.setSucCount(triggerDayCountSuc);
//                                                JobLogReport.setFailCount(triggerDayCountFail);
//                                            }
//
//                                            // do refresh
//                                            int ret =
//                                                    ApplicationContextHolder.getBean(JobAdminConfig.class).getJobLogReportDao().update(JobLogReport);
//                                            if (ret < 1) {
//                                                ApplicationContextHolder.getBean(JobAdminConfig.class).getJobLogReportDao().save(JobLogReport);
//                                            }
                                        }

                                    } catch (Exception e) {
                                        if (!toStop) {
                                        }
                                    }

                                    // 2、log-clean: switch open & once each day
                                    if (System.currentTimeMillis() - lastCleanLogTime > 24 * 60 * 60 * 1000) {

                                        // expire-time
                                        Calendar expiredDay = Calendar.getInstance();
                                        expiredDay.add(
                                                Calendar.DAY_OF_MONTH,
                                                -1 * 7);
                                        expiredDay.set(Calendar.HOUR_OF_DAY, 0);
                                        expiredDay.set(Calendar.MINUTE, 0);
                                        expiredDay.set(Calendar.SECOND, 0);
                                        expiredDay.set(Calendar.MILLISECOND, 0);
                                        Date clearBeforeTime = expiredDay.getTime();

                                        // clean expired log
                                        List<Long> logIds = null;
                                        do {
//                                            logIds =
//                                                    ApplicationContextHolder.getBean(JobAdminConfig.class)
//                                                            .getJobLogDao()
//                                                            .findClearLogIds(0, 0, clearBeforeTime, 0, 1000);
//                                            if (logIds != null && logIds.size() > 0) {
//                                                ApplicationContextHolder.getBean(JobLogDao.class).clearLog(logIds);
//                                            }
                                        } while (logIds != null && logIds.size() > 0);

                                        // update clean time
                                        lastCleanLogTime = System.currentTimeMillis();
                                    }

                                    try {
                                        TimeUnit.MINUTES.sleep(1);
                                    } catch (Exception e) {
                                        if (!toStop) {
                                            log.error(e.getMessage(), e);
                                        }
                                    }
                                }
                            }
                        });
        logrThread.setDaemon(true);
        logrThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        logrThread.interrupt();
        try {
            logrThread.join();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}
