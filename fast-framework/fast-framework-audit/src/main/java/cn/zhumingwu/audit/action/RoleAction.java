package cn.zhumingwu.audit.action;

import cn.zhumingwu.audit.action.base.BaseActionMap;
import cn.zhumingwu.audit.action.base.ResetLog;
import cn.zhumingwu.audit.action.model.BusinessMethod;


/**
 * 角色日志行为
 *
 */
public class RoleAction extends BaseActionMap {

    public static final String ROLE_SAVE = "role_save";
    public static final String ROLE_AUTH = "role_auth";

    @Override
    public void init() {
        // 保存日志行为
        putMethod(ROLE_SAVE, new BusinessMethod("日志管理", "roleSave"));
        // 角色授权行为
        putMethod(ROLE_AUTH, new BusinessMethod("角色授权", "roleAuth"));
    }

    /**
     * 保存用户行为方法
     */
    public void roleSave(ResetLog resetLog) {

        SaveAction.defaultMethod(resetLog);
    }

    /**
     * 角色授权行为方法
     */
    public void roleAuth(ResetLog resetLog) {

    }
}
