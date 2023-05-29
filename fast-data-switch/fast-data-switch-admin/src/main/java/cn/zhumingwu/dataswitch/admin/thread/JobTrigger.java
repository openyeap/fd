package cn.zhumingwu.dataswitch.admin.thread;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import cn.zhumingwu.dataswitch.admin.enums.TriggerType;
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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JobTrigger implements SmartLifecycle {


    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ConcurrentMap<Long, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();

    // fast/slow thread pool
    private final ThreadPoolExecutor fastTriggerPool;
    private final ThreadPoolExecutor slowTriggerPool;
    private final CoordinatorContext context;

    public JobTrigger(CoordinatorContext context) {
        this.context = context;
        this.fastTriggerPool =
                new ThreadPoolExecutor(
                        10,
                        200,
                        60L,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(1000),
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return null;
                            }
                        });

        this.slowTriggerPool =
                new ThreadPoolExecutor(
                        10,
                        10,
                        60L,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(2000),
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return null;
                            }
                        });
    }

    public void start() {

    }


    /**
     * add trigger
     *
     * @param jobId
     * @param triggerType
     * @param executorExpression null: use job param not null: cover job param
     * @param executorParam      null: use job param not null: cover job param
     * @param retryTimes         >=0: use this param <0: use param from job info config
     */
    public void addTrigger(Long jobId, TriggerType triggerType, List<String> executorExpression, List<String> executorParam, int retryTimes) {

        // 选择任务触发的线程池
        ThreadPoolExecutor triggerPool_ = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        if (jobTimeoutCount != null && jobTimeoutCount.get() > 10) {
            // 经常超时的任务使用低优先级的线程池（一分钟超过10次超时）
            triggerPool_ = slowTriggerPool;
        }
        var run =new JobTriggerRunnable(this.context, jobId, triggerType, executorExpression, executorParam, retryTimes);
        // trigger
        triggerPool_.execute(run);
    }


    @Override
    public void stop() {
        // 1、stop schedule
        if (this.running.compareAndSet(true, false)) {
            fastTriggerPool.shutdownNow();
            slowTriggerPool.shutdownNow();

        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }


    private class JobTriggerRunnable implements Runnable {
        // job timeout count
        private volatile long minTim = System.currentTimeMillis() / 60000; // ms > min

        private final CoordinatorContext context;
        private final Long jobId;
        private final TriggerType triggerType;
        private final List<String> executorExpression;
        private final List<String> executorParam;
        private final int retryTimes;

        public JobTriggerRunnable(CoordinatorContext context, Long jobId, TriggerType triggerType, List<String> executorExpression, List<String> executorParam, int retryTimes) {
            this.context = context;
            this.jobId = jobId;
            this.triggerType = triggerType;
            this.executorExpression = executorExpression;
            this.executorParam = executorParam;
            this.retryTimes = retryTimes;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();

            try {
                // do trigger
                this.context.trigger(this.jobId, this.triggerType, this.executorExpression, this.executorParam, this.retryTimes);
            } catch (Exception e) {
                log.error("error", e);
            } finally {

                // check timeout-count-map
                long minTim_now = System.currentTimeMillis() / 60000;
                if (minTim != minTim_now) {
                    minTim = minTim_now;
                    jobTimeoutCountMap.clear();
                }

                // incr timeout-count-map
                long cost = System.currentTimeMillis() - start;
                if (cost > 500) { // ob-timeout threshold 500ms
                    AtomicInteger timeoutCount = jobTimeoutCountMap.putIfAbsent(jobId, new AtomicInteger(1));
                    if (timeoutCount != null) {
                        timeoutCount.incrementAndGet();
                    }
                }
            }
        }
    }
}
