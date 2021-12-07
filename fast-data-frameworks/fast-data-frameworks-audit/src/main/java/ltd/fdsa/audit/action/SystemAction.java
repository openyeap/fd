package ltd.fdsa.audit.action;

import ltd.fdsa.audit.action.base.BaseActionMap;
import ltd.fdsa.audit.action.base.ResetLog;
import ltd.fdsa.audit.action.model.SystemMethod;

public class SystemAction extends BaseActionMap {

    public static final String RUNTIME_EXCEPTION = "runtime_exception";

    @Override
    public void init() {
        // 系统异常行为
        putMethod(RUNTIME_EXCEPTION, new SystemMethod("系统异常", "runtimeException"));
    }

    /**
     * 系统异常行为方法
     */
    public void runtimeException(ResetLog resetLog) {
        RuntimeException runtime = (RuntimeException) resetLog.getParam("e");
        StringBuilder message = new StringBuilder();
        message.append(runtime.toString());
        StackTraceElement[] stackTrace = runtime.getStackTrace();
        for (StackTraceElement stack : stackTrace) {
            message.append("\n\t").append(stack.toString());
        }
//        ActionLog actionLog = resetLog.getActionLog();
//        actionLog.setOperName("系统");
//        actionLog.setMessage(String.valueOf(message));
    }
}
