package cn.zhumingwu.dataswitch.core.job.thread;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.enums.JobStatus;
import cn.zhumingwu.dataswitch.core.job.executor.JobContext;
import cn.zhumingwu.dataswitch.core.job.executor.JobExecutor;
import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.core.job.model.CallbackParam;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.print.attribute.standard.JobSheets;

/**
 * Job Handler Thread
 */
@Slf4j
public class JobThread extends Thread {

    private final Long jobId;
    private final IJobHandler handler;

    // 使用消息队列
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
    public Result<String> pushTriggerQueue(TriggerParam triggerParam) {
        // avoid repeat
        if (triggerLogIdSet.contains(triggerParam.getTaskId())) {
            return Result.fail(500, "repeat job task, task id:" + triggerParam.getTaskId());
        }
        triggerLogIdSet.add(triggerParam.getTaskId());
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
         * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
         * 在阻塞中抛出InterruptedException异常,但是并不会终止运行的线程本身；
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
        // handler 的 init, 只一次。
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
                    JobContext.getJobContext().log("JobExecutor start\nParams:{}", triggerParam.getParams());
                    if (triggerParam.getTimeout() > 0) {
                        // limit timeout
                        Thread futureThread = null;
                        try {
                            FutureTask<Result<Object>> futureTask = new FutureTask<Result<Object>>(
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
                            JobContext.getJobContext().log("JobExecutor execute timeout\nError:{}", e.getMessage());

                        } finally {
                            if (futureThread != null) {
                                futureThread.interrupt();
                            }
                        }
                    } else {
                        handler.execute();
                    }

                } else {
                    if (idleTimes > 30) {
                        if (triggerQueue.size() == 0) { // 避免并发触发了误操作从而导致任务被中止
                            JobExecutor.stopJob(jobId, "excutor idel times over limit.");
                        }
                    }
                }
            } catch (Throwable e) {
                JobContext.getJobContext().log("JobThread toStop, stopReason:" + stopReason);
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                String errorMsg = stringWriter.toString();
                JobContext.getJobContext().log("JobThread Exception:{}", errorMsg);
            } finally {
                if (triggerParam != null) {
                    // callback handler info
                    if (!isRun.get()) {
                        // commonm
                        var callbackParam = new CallbackParam(triggerParam.getJobId(), triggerParam.getTaskId(), triggerParam.getHandler(), JobStatus.SUCCESS, 1, "common stop", System.currentTimeMillis());
                        ApplicationContextHolder.getBean(TriggerCallbackThread.class).pushCallBack(callbackParam);
                    } else {
                        // is killed
                        var callbackParam = new CallbackParam(triggerParam.getJobId(), triggerParam.getTaskId(), triggerParam.getHandler(), JobStatus.FAILED, -1, stopReason + " [job running, killed]", System.currentTimeMillis());
                        ApplicationContextHolder.getBean(TriggerCallbackThread.class).pushCallBack(callbackParam);
                    }
                }
            }
        }

        // callback trigger request in queue
        while (triggerQueue != null && triggerQueue.size() > 0) {
            TriggerParam triggerParam = triggerQueue.poll();
            if (triggerParam != null) {
                // is killed
                var callbackParam = new CallbackParam(triggerParam.getJobId(), triggerParam.getTaskId(), triggerParam.getHandler(), JobStatus.FAILED, -1, "[job not executed, in the job queue, killed.]", System.currentTimeMillis());
                ApplicationContextHolder.getBean(TriggerCallbackThread.class).pushCallBack(callbackParam);
            }
        }

        // destroy
        try {
            handler.destroy();
        } catch (Throwable e) {
            log.error("error", e);
        }
        log.info(">>>>>>>>>>> project.JobThread stoped, hashCode:{}", Thread.currentThread());
    }
}
