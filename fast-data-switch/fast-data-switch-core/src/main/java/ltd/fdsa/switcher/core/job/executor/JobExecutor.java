package ltd.fdsa.switcher.core.job.executor;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.switcher.core.job.coordinator.Coordinator;
import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.switcher.core.job.log.JobFileAppender;
import ltd.fdsa.switcher.core.job.coordinator.CoordinatorClient;
import ltd.fdsa.switcher.core.job.thread.JobThread;
import ltd.fdsa.switcher.core.job.thread.TriggerCallbackThread;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 客户端执行器
 */
@Slf4j
public class JobExecutor {
    // ---------------------- job handler repository ----------------------
    private static ConcurrentMap<String, JobHandler> jobHandlerRepository = new ConcurrentHashMap<String, JobHandler>();
    // ---------------------- job thread repository ----------------------
    private static ConcurrentMap<Integer, JobThread> jobThreadRepository = new ConcurrentHashMap<Integer, JobThread>();
    private final static List<Coordinator> coordinators = new LinkedList<>();

    public static List<Coordinator> getCoordinators() {
        return coordinators;
    }

    private final String appName;
    private final String ip;
    private final int port;
    private final String accessToken;
    private final String logPath;
    private final int logRetentionDays;

    public JobExecutor(Properties properties) {
        this.appName = properties.getProperty("name", "");
        this.ip = properties.getProperty("ip");
        this.port = Integer.parseInt(properties.getProperty("port", "8080"));
        this.logPath = properties.getProperty("log_path", "./logs");
        this.logRetentionDays = Integer.parseInt(properties.getProperty("log_days", "7"));
        this.accessToken = properties.getProperty("access_token", "");
        this.coordinators.addAll(Arrays.stream(properties.getProperty("", "").split(",")).map(m -> new CoordinatorClient(m, this.accessToken)).collect(Collectors.toList()));
    }


    /**
     * 注册本地Job Handler
     */
    public static JobHandler registerJobHandler(String name, JobHandler jobHandler) {
        return jobHandlerRepository.put(name, jobHandler);
    }

    public static JobHandler loadJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }

    public static JobThread startJob(int jobId, JobHandler handler, String... reasons) {
        //如果job已经运行，需要停止运行
        stopJob(jobId, reasons);
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();
        return jobThreadRepository.put(jobId, newJobThread);
    }

    public static void stopJob(int jobId, String... removeOldReason) {
        JobThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(String.join("\n", removeOldReason));
            oldJobThread.interrupt();
        }
    }

    public static JobThread loadJobThread(int jobId) {
        JobThread jobThread = jobThreadRepository.get(jobId);
        return jobThread;
    }


    public void start() throws Exception {
        // init logpath
        JobFileAppender.initLogPath(logPath);



        // init TriggerCallbackThread
        TriggerCallbackThread.getInstance().start();

        // init executor-server todo

    }

    public void destroy() {
        // destory executor-server

        // destory jobThreadRepository
        if (jobThreadRepository.size() > 0) {
            for (Map.Entry<Integer, JobThread> item : jobThreadRepository.entrySet()) {
                stopJob(item.getKey(), "web container destroy and kill the job.");
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();

        // destory TriggerCallbackThread
        TriggerCallbackThread.getInstance().toStop();
    }

}
