package ltd.fdsa.switcher.core.job.handler;

import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface JobHandler {

    /**
     * success
     */
    public static final Result<Object> SUCCESS = Result.success();
    /**
     * fail
     */
    public static final Result<Object> FAIL = Result.fail(500, null);
    /**
     * fail timeout
     */
    public static final Result<Object> FAIL_TIMEOUT = Result.fail(HttpCode.REQUEST_TIMEOUT);


    Result<Object> execute(Map<String, String> context) ;

    /**
     * init handler, invoked when JobThread init
     */
    default void init() {

    }

    /**
     * destroy handler, invoked when JobThread destroy
     */
    default void destroy() {
    }
}
