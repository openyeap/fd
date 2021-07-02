package ltd.fdsa.switcher.core.job.handler.impl;

import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.switcher.core.job.log.JobLogger;
import ltd.fdsa.web.view.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * glue job handler
 */
public class GlueJobHandler implements JobHandler {

    private JobHandler jobHandler;

    public GlueJobHandler(JobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }


    @Override
    public Result<Object> execute(Map<String, String> context) {
        return jobHandler.execute(context);
    }
}
