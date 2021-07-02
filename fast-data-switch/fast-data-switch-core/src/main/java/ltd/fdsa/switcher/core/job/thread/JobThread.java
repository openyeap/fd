package ltd.fdsa.switcher.core.job.thread;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.switcher.core.job.executor.JobExecutor;
import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.switcher.core.job.log.JobFileAppender;
import ltd.fdsa.switcher.core.job.log.JobLogger;
import ltd.fdsa.switcher.core.job.model.HandleCallbackParam;
import ltd.fdsa.switcher.core.job.model.TriggerParam;
import ltd.fdsa.switcher.core.util.ShardingUtil;
import ltd.fdsa.web.view.Result;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Job Handler Thread
 */
@Slf4j
public class JobThread extends Thread {

    private int jobId;
    private JobHandler handler;

    //使用消息队列
    private LinkedBlockingQueue<TriggerParam> triggerQueue;
    private Set<Long> triggerLogIdSet; // avoid repeat trigger for the same TRIGGER_LOG_ID
    private final AtomicBoolean isRun = new AtomicBoolean(false);

    private String stopReason;

    private int idleTimes = 0; // idel times

    public JobThread(int jobId, JobHandler handler) {
        this.jobId = jobId;
        this.handler = handler;
        this.triggerQueue = new LinkedBlockingQueue<TriggerParam>();
        this.triggerLogIdSet = Collections.synchronizedSet(new HashSet<Long>());
    }

    public JobHandler getHandler() {
        return handler;
    }

    /**
     * new trigger to queue
     *
     * @param triggerParam
     * @return
     */
    public Result<String> pushTriggerQueue(TriggerParam triggerParam) {
        // avoid repeat
        if (triggerLogIdSet.contains(triggerParam.getLogId())) {
            log.info(">>>>>>>>>>> repeate trigger job, logId:{}", triggerParam.getLogId());
            return Result.fail(500, "repeate trigger job, logId:" + triggerParam.getLogId());
        }

//        triggerLogIdSet.add(triggerParam);
        triggerQueue.add(triggerParam);
        return Result.success();
    }

    /**
     * kill job thread
     *
     * @param stopReason
     */
    public void toStop(String stopReason) {
        /**
         * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)， 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
         * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
         */
        this.isRun.set(false);
        this.stopReason = stopReason;
    }

    /**
     * is running job
     *
     * @return
     */
    public boolean isRunningOrHasQueue() {
        return this.isRun.get() || triggerQueue.size() > 0;
    }

    @Override
    public void run() {
        if (!this.isRun.compareAndSet(false, true)) {
            return;
        }
        // init
        try {
            handler.init();
        } catch (Throwable e) {
            log.error("handler.init failed", e);
        }

        // execute
        while (this.isRun.get()) {

            idleTimes++;

            TriggerParam triggerParam = null;
            Result<Object> executeResult = null;
            try {
                // to check toStop signal, we need cycle, so wo cannot use queue.take(), instand of
                // poll(timeout)
                triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
                if (triggerParam != null) {

                    idleTimes = 0;
                    triggerLogIdSet.remove(triggerParam.getLogId());
                    // log filename, like "logPath/yyyy-MM-dd/9999.log"
                    String logFileName = JobFileAppender.makeLogFileName(new Date(triggerParam.getLogDateTime()), triggerParam.getLogId());
                    JobFileAppender.contextHolder.set(logFileName);
                    ShardingUtil.setShardingVo(new ShardingUtil.ShardingVO(triggerParam.getBroadcastIndex(), triggerParam.getBroadcastTotal()));

                    // execute
                    JobLogger.log("<br>----------- project.job execute start -----------<br>----------- Param:" + triggerParam.getExecutorParams());

                    if (triggerParam.getExecutorTimeout() > 0) {
                        // limit timeout
                        Thread futureThread = null;
                        try {
                            final TriggerParam triggerParamTmp = triggerParam;
                            FutureTask<Result<Object>> futureTask =
                                    new FutureTask<Result<Object>>(
                                            new Callable<Result<Object>>() {
                                                @Override
                                                public Result<Object> call() throws Exception {
                                                    return handler.execute(triggerParamTmp.getExecutorParams());
                                                }
                                            });
                            futureThread = new Thread(futureTask);
                            futureThread.start();

                            executeResult = futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
                        } catch (TimeoutException e) {

                            JobLogger.log("<br>----------- project.job execute timeout");
                            JobLogger.log(e);

                            executeResult = Result.fail(JobHandler.FAIL_TIMEOUT.getCode(), "job execute timeout ");
                        } finally {
                            futureThread.interrupt();
                        }
                    } else {
                        // just execute
                        executeResult = handler.execute(triggerParam.getExecutorParams());
                    }

                    if (executeResult == null) {
                        executeResult = JobHandler.FAIL;
                    } else {
                        executeResult.setMessage(
                                (executeResult != null
                                        && executeResult.getMessage() != null
                                        && executeResult.getMessage().length() > 50000)
                                        ? executeResult.getMessage().substring(0, 50000).concat("...")
                                        : executeResult.getMessage());
                        executeResult.setData(null); // limit obj size
                    }
                    JobLogger.log(
                            "<br>----------- project.job execute end(finish) -----------<br>----------- Result:"
                                    + executeResult);

                } else {
                    if (idleTimes > 30) {
                        if (triggerQueue.size() == 0) { // avoid concurrent trigger causes jobId-lost
                            JobExecutor.stopJob(jobId, "excutor idel times over limit.");
                        }
                    }
                }
            } catch (Throwable e) {

                JobLogger.log("<br>----------- JobThread toStop, stopReason:" + stopReason);


                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                String errorMsg = stringWriter.toString();
                executeResult = Result.fail(500, errorMsg);

                JobLogger.log(
                        "<br>----------- JobThread Exception:"
                                + errorMsg
                                + "<br>----------- project.job execute end(error) -----------");
            } finally {
                if (triggerParam != null) {
                    // callback handler info
                    if (!isRun.get()) {
                        // commonm
                        HandleCallbackParam p = new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTime(), executeResult);
                        TriggerCallbackThread.pushCallBack(p);
                    } else {
                        // is killed
                        Result<Object> stopResult =
                                Result.fail(500, stopReason + " [job running, killed]");
                        TriggerCallbackThread.pushCallBack(
                                new HandleCallbackParam(triggerParam.getLogId(), triggerParam.getLogDateTime(), stopResult));
                    }
                }
            }
        }

        // callback trigger request in queue
        while (triggerQueue != null && triggerQueue.size() > 0) {
            TriggerParam triggerParam = triggerQueue.poll();
            if (triggerParam != null) {
                // is killed
                Result<Object> stopResult =
                        Result.fail(500, stopReason + " [job not executed, in the job queue, killed.]");
                TriggerCallbackThread.pushCallBack(
                        new HandleCallbackParam(
                                triggerParam.getLogId(), triggerParam.getLogDateTime(), stopResult));
            }
        }

        // destroy
        try {
            handler.destroy();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

        log.info(">>>>>>>>>>> project.JobThread stoped, hashCode:{}", Thread.currentThread());
    }
}
