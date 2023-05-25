package cn.zhumingwu.dataswitch.core.job.executor;


import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.core.job.thread.JobThread;
import lombok.var;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 客户端执行器
 */
@Slf4j
public class JobExecutor {
    // ---------------------- job handler repository ----------------------
    private static ConcurrentMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();
    // ---------------------- job thread repository ----------------------
    private static ConcurrentMap<Long, JobThread> jobThreadRepository = new ConcurrentHashMap<Long, JobThread>();


    public JobExecutor(Properties properties) {


    }

    /**
     * 注册本地Job Handler
     */
    public static void registerJobHandler(String name, IJobHandler jobHandler) {
        jobHandlerRepository.put(name, jobHandler);
    }

    public static IJobHandler loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }

    public static JobThread startJob(Long jobId, IJobHandler handler, String... reasons) {
        //如果job已经运行，需要停止运行
        stopJob(jobId, reasons);
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();
        return jobThreadRepository.put(jobId, newJobThread);
    }

    public static void stopJob(Long jobId, String... removeOldReason) {
        JobThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(String.join("\n", removeOldReason));
            oldJobThread.interrupt();
        }
    }

    public static JobThread loadJobThread(Long jobId) {
        return jobThreadRepository.get(jobId);
    }

    public void destroy() {
        // destory executor-server

        // destory jobThreadRepository
        if (jobThreadRepository.size() > 0) {
            for (var item : jobThreadRepository.entrySet()) {
                stopJob(item.getKey(), "web container destroy and kill the job.");
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();


    }

}
