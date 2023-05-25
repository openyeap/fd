package cn.zhumingwu.dataswitch.core.job.thread;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.executor.JobContext;
import cn.zhumingwu.dataswitch.core.job.executor.JobExecutor;
import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.dataswitch.core.job.model.HandleCallbackParam;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;

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

    private final Long jobId;
    private final IJobHandler handler;

    //使用消息队列
    private final LinkedBlockingQueue<TriggerParam> triggerQueue;
    private final Set<Long> triggerLogIdSet; // avoid repeat trigger for the same TRIGGER_LOG_ID
    private final AtomicBoolean isRun = new AtomicBoolean(false);

    private String stopReason;

    private int idleTimes = 0; // idle times

    public JobThread(Long jobId, IJobHandler handler) {
        this.jobId = jobId;
        this.handler = handler;
        this.triggerQueue = new LinkedBlockingQueue<TriggerParam>();
        this.triggerLogIdSet = Collections.synchronizedSet(new HashSet<Long>());
    }

    public IJobHandler getHandler() {
        return handler;
    }

    /**
     * new trigger to queue
     *
     * @param triggerParam
     * @return
     */
    public Result<Long> pushTriggerQueue(TriggerParam triggerParam) {
        // avoid repeat
        if (triggerLogIdSet.contains(triggerParam.getTaskId())) {
            return Result.fail(500, "repeat job task, task id:" + triggerParam.getTaskId());
        }
        triggerLogIdSet.add(triggerParam.getTaskId());
        triggerQueue.add(triggerParam);
        return Result.success(triggerParam.getTaskId());
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

        // 为了可以检测到 toStop ,需要通过循环获取触发队列poll(timeout)
        while (this.isRun.get()) {
            idleTimes++;

            TriggerParam triggerParam = null;

            try {
                triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
                if (triggerParam != null) {

                    idleTimes = 0;
                    triggerLogIdSet.remove(triggerParam.getTaskId());
                    // execute
                    JobContext.setJobContext(triggerParam);
                    JobContext.getJobContext().log("<br>----------- project.job execute start -----------<br>----------- Param:" + triggerParam.getParams());
                    if (triggerParam.getTimeout() > 0) {
                        // limit timeout
                        Thread futureThread = null;
                        try {
                            FutureTask<Result<Object>> futureTask =
                                    new FutureTask<Result<Object>>(
                                            new Callable<Result<Object>>() {
                                                @Override
                                                public Result<Object> call() throws Exception {

                                                    handler.execute();
                                                    return Result.success();
                                                }
                                            });
                            futureThread = new Thread(futureTask);
                            futureThread.start();
                            futureTask.get(triggerParam.getTimeout(), TimeUnit.SECONDS);
                        } catch (TimeoutException e) {

                            JobContext.getJobContext().log("<br>----------- project.job execute timeout");
                            JobContext.getJobContext().log(e);

                        } finally {
                            if (futureThread != null)
                                futureThread.interrupt();
                        }
                    } else {
                        handler.execute();
                    }


                } else {
                    if (idleTimes > 30) {
                        if (triggerQueue.size() == 0) { // avoid concurrent trigger causes jobId-lost
                            JobExecutor.stopJob(jobId, "excutor idel times over limit.");
                        }
                    }
                }
            } catch (Throwable e) {

                JobContext.getJobContext().log("<br>----------- JobThread toStop, stopReason:" + stopReason);


                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                String errorMsg = stringWriter.toString();


                JobContext.getJobContext().log(
                        "<br>----------- JobThread Exception:"
                                + errorMsg
                                + "<br>----------- project.job execute end(error) -----------");
            } finally {
                if (triggerParam != null) {
                    // callback handler info
                    if (!isRun.get()) {
                        // commonm
                        HandleCallbackParam p = new HandleCallbackParam(triggerParam.getTaskId(), triggerParam.getTimestamp(), Result.success());
                        TriggerCallbackThread.pushCallBack(p);
                    } else {
                        // is killed
                        Result<Object> stopResult =
                                Result.fail(500, stopReason + " [job running, killed]");
                        TriggerCallbackThread.pushCallBack(
                                new HandleCallbackParam(triggerParam.getTaskId(), triggerParam.getTimestamp(), stopResult));
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
                                triggerParam.getTaskId(), triggerParam.getTimestamp(), stopResult));
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
