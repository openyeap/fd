package cn.zhumingwu.audit.action;

import cn.zhumingwu.audit.action.base.BaseActionMap;
import cn.zhumingwu.audit.action.base.ResetLog;
import lombok.extern.slf4j.Slf4j;


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
