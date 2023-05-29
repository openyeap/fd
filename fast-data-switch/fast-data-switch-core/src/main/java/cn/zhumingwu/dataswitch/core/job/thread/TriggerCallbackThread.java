package cn.zhumingwu.dataswitch.core.job.thread;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.coordinator.Coordinator;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.core.job.model.CallbackParam;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.TaskScheduler;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TriggerCallbackThread implements SmartLifecycle {
    private ScheduledFuture<?> serviceWatchFuture;
    private final Coordinator client;

    /**
     * job results callback queue
     */
    private final LinkedBlockingQueue<CallbackParam> callBackQueue = new LinkedBlockingQueue<CallbackParam>();
    private final TaskScheduler taskScheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);

    private TriggerCallbackThread(Coordinator client, TaskScheduler taskScheduler) {
        this.client = client;
        this.taskScheduler = taskScheduler;
    }


    public void pushCallBack(CallbackParam callback) {
        this.callBackQueue.add(callback);
    }


    // ---------------------- fail-callback file ----------------------

    /**
     * do callback, will retry if error
     *
     * @param callbackParamList
     */
    private void doCallback(List<CallbackParam> callbackParamList) {

        try {
            Result<String> callbackResult = client.callback(callbackParamList);
            if (callbackResult == null || callbackResult.getCode() != Result.OK) {
                log.error("failed", callbackParamList);
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    /**
     * callback log
     */
    private void callbackLog() {
        if (!this.running.get()) {
            return;
        }

        try {
            CallbackParam callback = this.callBackQueue.take();
            // callback list param
            List<CallbackParam> callbackParamList = new ArrayList<CallbackParam>();
            int drainToNum = this.callBackQueue.drainTo(callbackParamList);
            callbackParamList.add(callback);
            doCallback(callbackParamList);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void start() {

        if (this.running.compareAndSet(false, true)) {
            this.serviceWatchFuture = this.taskScheduler.scheduleWithFixedDelay(this::callbackLog, Duration.ofSeconds(5));
        }
    }


    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            if (this.serviceWatchFuture != null) {
                this.serviceWatchFuture.cancel(true);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }
}
