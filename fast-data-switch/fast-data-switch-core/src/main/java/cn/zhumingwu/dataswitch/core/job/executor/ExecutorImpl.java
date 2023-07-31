package cn.zhumingwu.dataswitch.core.job.executor;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.handler.IJobHandler;
import cn.zhumingwu.dataswitch.core.util.JobFileAppender;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import cn.zhumingwu.dataswitch.core.job.model.LogResult;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import cn.zhumingwu.dataswitch.core.job.thread.JobThread;

import java.util.Map;

/**
 * 客户端具体实现
 */
@Slf4j
public class ExecutorImpl implements Executor {
    @Override
    public Result<String> start(TriggerParam param) {

        String handlerName = param.getHandler();
        if (Strings.isNullOrEmpty(handlerName)) {
            return Result.fail(404, "no job handler");
        }
        // new job handler
        var newJobHandler = JobExecutor.getJobHandler(handlerName);
        if (newJobHandler == null) {
            return Result.fail(404, "job handler [" + handlerName + "] not found.");
        }
        // load old jobHandler from jobThread
        IJobHandler jobHandler = null;
        JobThread jobThread = JobExecutor.loadJobThread(param.getJobId());
        if (jobThread != null) {
            jobHandler = jobThread.getHandler();
        }
        // valid old jobThread
        String removeOldReason = null;

        if (jobHandler != newJobHandler) {
            // change handler, need kill old thread
            removeOldReason = "change job handler or glue type, and terminate the old job thread.";
            jobThread = null;
            jobHandler = newJobHandler;
        }
        // executor block strategy
        if (jobThread != null) {
            var blockStrategy = param.getExecutorBlockStrategy();

            if (ExecutorBlockStrategy.DISCARD == blockStrategy) {
                // discard when running
                if (jobThread.isRunningOrHasQueue()) {
                    return Result.fail(500, "block strategy effect：" + ExecutorBlockStrategy.DISCARD.getName());
                }
            } else if (ExecutorBlockStrategy.COVER == blockStrategy) {
                // kill running jobThread
                if (jobThread.isRunningOrHasQueue()) {
                    removeOldReason = "block strategy effect：" + ExecutorBlockStrategy.COVER.getName();
                    jobThread = null;
                }
            } else {
                // just queue trigger
                return jobThread.pushTriggerQueue(param);
            }
        }

        // replace thread (new or exists invalid)
        if (jobThread == null) {
            jobThread = JobExecutor.startJob(param.getJobId(), jobHandler, removeOldReason);
        }
        // push data to queue
        return jobThread.pushTriggerQueue(param);
    }

    @Override
    public Result<String> stop(Long jobId, Long taskId) {
        // kill handlerThread, and create new one
        JobThread jobThread = JobExecutor.loadJobThread(jobId);
        if (jobThread != null) {
            JobExecutor.stopJob(jobId, "scheduling center kill job.");
            return Result.success();
        }

        return Result.success("job thread already killed.");
    }

    @Override
    public Result<Map<String, String>> stat() {
        return null;
    }

    @Override
    public Result<Map<String, String>> stat(Long taskId) {
        boolean isRunningOrHasQueue = false;
        JobThread jobThread = JobExecutor.loadJobThread(taskId);
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            isRunningOrHasQueue = true;
        }
        if (isRunningOrHasQueue) {
            return Result.fail(500, "job thread is running or has trigger queue.");
        }
        return Result.success();
    }

    @Override
    public Result<LogResult> stat(String handler, Long jobId, Long taskId, long timestamp, Long lastVersion) {
        String logFileName = JobFileAppender.getLogFile(handler, jobId, taskId, timestamp);
        LogResult logResult = JobFileAppender.readLog(logFileName, lastVersion.intValue());
        return Result.success(logResult);
    }
}
