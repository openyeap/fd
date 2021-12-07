package ltd.fdsa.audit.action;

import ltd.fdsa.audit.action.base.BaseActionMap;
import ltd.fdsa.audit.action.base.ResetLog;

/**
 * 通用：记录数据状态的行为
 */
public class StatusAction extends BaseActionMap {

    /**
     * 重新包装保存的数据行为方法
     *
     * @param resetLog ResetLog对象数据
     */
    @SuppressWarnings("unchecked")
    public static void defaultMethod(ResetLog resetLog) {
        if (resetLog.isSuccessRecord()) {
            String param = (String) resetLog.getParam("param");
//            StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
//            List<Long> ids = (List<Long>) resetLog.getParam("ids");
//            resetLog.getActionLog().setMessage(statusEnum.getMessage() + "ID：" + ids.toString());
        }
    }

    @Override
    public void init() {
        // 记录数据状态改变日志
        putMethod("default", "defaultMethod");
    }
}
