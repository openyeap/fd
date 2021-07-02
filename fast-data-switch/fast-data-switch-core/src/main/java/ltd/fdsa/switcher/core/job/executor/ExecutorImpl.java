package ltd.fdsa.switcher.core.job.executor;

import lombok.var;
import ltd.fdsa.switcher.core.job.model.LogResult;
import ltd.fdsa.switcher.core.job.model.TriggerParam;
import ltd.fdsa.switcher.core.job.enums.ExecutorBlockStrategyEnum;

import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.switcher.core.job.log.JobFileAppender;
import ltd.fdsa.switcher.core.job.thread.JobThread;
import ltd.fdsa.web.view.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 客户端具体实现
 *
 */
public class ExecutorImpl implements Executor {
    private static Logger logger = LoggerFactory.getLogger(ExecutorImpl.class);

    @Override
    public Result<String> beat() {
        return Result.success();
    }

    @Override
    public Result<String> idleBeat(int jobId) {

        // isRunningOrHasQueue
        boolean isRunningOrHasQueue = false;
        JobThread jobThread = JobExecutor.loadJobThread(jobId);
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            isRunningOrHasQueue = true;
        }

        if (isRunningOrHasQueue) {
            return Result.fail(500, "job thread is running or has trigger queue.");
        }
        return Result.success();
    }

    @Override
    public Result<String> kill(int jobId) {
        // kill handlerThread, and create new one
        JobThread jobThread = JobExecutor.loadJobThread(jobId);
        if (jobThread != null) {
            JobExecutor.stopJob(jobId, "scheduling center kill job.");
            return Result.success();
        }

        return Result.success("job thread already killed.");
    }

    @Override
    public Result<LogResult> log(int jobId, String lastVersion) {
        String logFileName = JobFileAppender.getLogFile(jobId);

        LogResult logResult = JobFileAppender.readLog(logFileName, Integer.parseInt(lastVersion));
        return Result.success(logResult);
    }

    @Override
    public Result<String> run(int jobId, String handler, String blockStrategy, long timeout, Map<String, String> content) {
        // load old：jobHandler + jobThread
        JobThread jobThread = JobExecutor.loadJobThread(jobId);
        JobHandler jobHandler = jobThread != null ? jobThread.getHandler() : null;
        String removeOldReason = null;

        // new jobhandler
        JobHandler newJobHandler = JobExecutor.loadJobHandler(handler);

        // valid old jobThread
        if (jobThread != null && jobHandler != newJobHandler) {
            // change handler, need kill old thread
            removeOldReason = "change jobhandler or glue type, and terminate the old job thread.";

            jobThread = null;
            jobHandler = null;
        }

        // valid handler
        if (jobHandler == null) {
            jobHandler = newJobHandler;
            if (jobHandler == null) {
                return Result.fail(500,
                        "job handler [" + handler + "] not found.");
            }
        }


        // executor block strategy
        if (jobThread != null) {
            ExecutorBlockStrategyEnum blockStrategyEnum =
                    ExecutorBlockStrategyEnum.match(blockStrategy, null);
            if (ExecutorBlockStrategyEnum.DISCARD_LATER == blockStrategyEnum) {
                // discard when running
                if (jobThread.isRunningOrHasQueue()) {
                    return Result.fail(500,
                            "block strategy effect：" + ExecutorBlockStrategyEnum.DISCARD_LATER.getTitle());
                }
            } else if (ExecutorBlockStrategyEnum.COVER_EARLY == blockStrategyEnum) {
                // kill running jobThread
                if (jobThread.isRunningOrHasQueue()) {
                    removeOldReason =
                            "block strategy effect：" + ExecutorBlockStrategyEnum.COVER_EARLY.getTitle();

                    jobThread = null;
                }
            } else {
                // just queue trigger
            }
        }

        // replace thread (new or exists invalid)
        if (jobThread == null) {
            jobThread = JobExecutor.startJob(jobId, jobHandler, removeOldReason);
        }

        // push data to queue
        var triggerParam    =new TriggerParam();
        triggerParam.setExecutorHandler(handler);
        triggerParam.setJobId(jobId);
        Result<String> pushResult = jobThread.pushTriggerQueue(triggerParam);
        return pushResult;
    }
}
