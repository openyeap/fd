package ltd.fdsa.job.admin.trigger;

import ltd.fdsa.job.admin.util.I18nUtil;

/**
 * trigger type enum
 */
public enum TriggerTypeEnum {
    MANUAL(I18nUtil.getString("jobconf_trigger_type_manual")),
    CRON(I18nUtil.getString("jobconf_trigger_type_cron")),
    RETRY(I18nUtil.getString("jobconf_trigger_type_retry")),
    PARENT(I18nUtil.getString("jobconf_trigger_type_parent")),
    API(I18nUtil.getString("jobconf_trigger_type_api"));

    private String title;

    private TriggerTypeEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
