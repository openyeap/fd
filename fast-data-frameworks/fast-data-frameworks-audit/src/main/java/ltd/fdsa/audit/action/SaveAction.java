package ltd.fdsa.audit.action;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.audit.action.base.BaseActionMap;
import ltd.fdsa.audit.action.base.ResetLog;


@Slf4j
public class SaveAction extends BaseActionMap {

    /**
     * 重新包装保存的数据行为方法
     *
     * @param resetLog ResetLog对象数据
     */
    public static void defaultMethod(ResetLog resetLog) {

    }

    @Override
    public void init() {
        // 记录数据保存日志
        putMethod("default", "defaultMethod");
    }
}
