package cn.zhumingwu.data.hub.admin.enums;

/**
 * trigger type enum
 */
public enum TriggerType {
    MANUAL("trigger_type_manual"),
    CRON("trigger_type_cron"),
    RETRY("trigger_type_retry"),
    PARENT("trigger_type_parent"),
    API("trigger_type_api");

    private final String title;

    TriggerType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

