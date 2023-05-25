package cn.zhumingwu.dataswitch.admin.enums;

import cn.zhumingwu.base.support.CommonEnum;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;

/**
 * trigger type enum
 */
public enum TriggerTypeEnum {
    MANUAL(I18nUtil.getInstance("").getString("jobconf_trigger_type_manual")),
    CRON(I18nUtil.getInstance("").getString("jobconf_trigger_type_cron")),
    RETRY(I18nUtil.getInstance("").getString("jobconf_trigger_type_retry")),
    PARENT(I18nUtil.getInstance("").getString("jobconf_trigger_type_parent")),
    API(I18nUtil.getInstance("").getString("jobconf_trigger_type_api"));

    private String title;

    private TriggerTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

