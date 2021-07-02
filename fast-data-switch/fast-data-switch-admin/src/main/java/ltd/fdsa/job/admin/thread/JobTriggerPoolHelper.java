package ltd.fdsa.job.admin.thread;

import ltd.fdsa.job.admin.trigger.JobTrigger;
import ltd.fdsa.job.admin.trigger.TriggerTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * job trigger thread pool helper
 */
public class JobTriggerPoolHelper {
    private static Logger logger = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    // ---------------------- trigger pool ----------------------
    private static JobTriggerPoolHelper helper = new JobTriggerPoolHelper();
    // fast/slow thread pool
    private ThreadPoolExecutor fastTriggerPool = null;
    private ThreadPoolExecutor slowTriggerPool = null;
    // job timeout count
    private volatile long minTim = System.currentTimeMillis() / 60000; // ms > min
    private volatile ConcurrentMap<Integer, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();

    public static void toStart() {
        helper.start();
    }

    public static void toStop() {
        helper.stop();
    }

    // ---------------------- helper ----------------------

    /**
     * @param jobId
     * @param triggerType
     * @param failRetryCount        >=0: use this param <0: use param from job info config
     * @param executorShardingParam
     * @param executorParam         null: use job param not null: cover job param
     */
    public static void trigger(
            int jobId,
            TriggerTypeEnum triggerType,
            int failRetryCount,
            String executorShardingParam,
            String executorParam) {
        helper.addTrigger(jobId, triggerType, failRetryCount, executorShardingParam, executorParam);
    }

    public void start() {
        fastTriggerPool =
                new ThreadPoolExecutor(
                        10,
                        20,
                        60L,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(1000),
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return null;
                            }
                        });

        slowTriggerPool =
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

    public void stop() {
        // triggerPool.shutdown();
        fastTriggerPool.shutdownNow();
        slowTriggerPool.shutdownNow();
    }

    /**
     * add trigger
     */
    public void addTrigger(final int jobId, final TriggerTypeEnum triggerType, final int failRetryCount, final String executorShardingParam, final String executorParam) {

        // choose thread pool
        ThreadPoolExecutor triggerPool_ = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        if (jobTimeoutCount != null && jobTimeoutCount.get() > 10) { // job-timeout 10 times in 1 min
            triggerPool_ = slowTriggerPool;
        }

        // trigger
        triggerPool_.execute(
                new Runnable() {
                    @Override
                    public void run() {

                        long start = System.currentTimeMillis();

                        try {
                            // do trigger
                            JobTrigger.trigger(jobId, triggerType, failRetryCount, executorShardingParam, executorParam);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
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
                });
    }
}
