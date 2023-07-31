package cn.zhumingwu.dataswitch.core.job.executor;


import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.core.job.thread.JobThread;


import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 客户端执行器
 */
@Slf4j
public class JobExecutor {
    // ---------------------- job handler repository ----------------------
    private static final ConcurrentMap<String, IJobHandler> JOB_HANDLER_MAP = new ConcurrentHashMap<String, IJobHandler>();
    // ---------------------- job thread repository ----------------------
    private static final ConcurrentMap<Long, JobThread> JOB_TASK_THREAD_MAP = new ConcurrentHashMap<Long, JobThread>();

    public JobExecutor(Properties properties) {

    }

    /**
     * 注册本地Job Handler
     */
    public static void registerJobHandler(String name, IJobHandler jobHandler) {
        JOB_HANDLER_MAP.put(name, jobHandler);
    }

    /**
     * 获取本地Job Handler
     */

    public static IJobHandler getJobHandler(String name) {
        return JOB_HANDLER_MAP.get(name);
    }

    public static JobThread startJob(Long jobId, IJobHandler handler, String... reasons) {
        //如果job已经运行，需要停止运行
        stopJob(jobId, reasons);
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();
        return JOB_TASK_THREAD_MAP.put(jobId, newJobThread);
    }

    public static void stopJob(Long jobId, String... removeOldReason) {
        JobThread oldJobThread = JOB_TASK_THREAD_MAP.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(String.join("\n", removeOldReason));
            oldJobThread.interrupt();
        }
    }

    public static JobThread loadJobThread(Long jobId) {
        return JOB_TASK_THREAD_MAP.get(jobId);
    }

    public void destroy() {
        // destory executor-server

        // destory jobThreadRepository
        if (JOB_TASK_THREAD_MAP.size() > 0) {
            for (var item : JOB_TASK_THREAD_MAP.entrySet()) {
                stopJob(item.getKey(), "web container destroy and kill the job.");
            }
            JOB_TASK_THREAD_MAP.clear();
        }
        JOB_HANDLER_MAP.clear();


    }

}
