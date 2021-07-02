package ltd.fdsa.switcher.core.job.handler.impl;

import lombok.var;
import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.switcher.core.job.log.JobFileAppender;
import ltd.fdsa.switcher.core.util.ScriptUtil;
import ltd.fdsa.web.view.Result;

import java.io.IOException;
import java.util.Map;

public class ScriptJobHandler implements JobHandler {

    @Override
    public Result<Object> execute(Map<String, String> context)   {
        // log file
        String logFileName = JobFileAppender.contextHolder.get();
        var cmd = context.get("cmd");
        var scriptFileName = context.get("scriptFileName");
        var scriptParams = context.get("scriptParams");
        int exitValue = 0;
        try {
            exitValue = ScriptUtil.execToFile(cmd, scriptFileName, logFileName, scriptParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exitValue == 0) {
            return Result.success();
        } else {
            return Result.fail(JobHandler.FAIL.getCode(), "script exit value(" + exitValue + ") is failed");
        }
    }
}
