package ltd.fdsa.audit.exception;

import ltd.fdsa.audit.action.SystemAction;
import ltd.fdsa.audit.annotation.ActionLog;
import ltd.fdsa.web.exception.ExceptionAdvice;

/**
 * 运行时抛出的异常进行日志记录
 *
 */
public class ActionLogProceedAdvice implements ExceptionAdvice {

    @Override
    @ActionLog(key = SystemAction.RUNTIME_EXCEPTION, action = SystemAction.class)
    public void run(RuntimeException e) {
    }
}
