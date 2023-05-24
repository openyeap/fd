package cn.zhumingwu.audit.exception;

import cn.zhumingwu.audit.annotation.ActionLog;
import cn.zhumingwu.audit.action.SystemAction;
import cn.zhumingwu.web.exception.ExceptionAdvice;

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
