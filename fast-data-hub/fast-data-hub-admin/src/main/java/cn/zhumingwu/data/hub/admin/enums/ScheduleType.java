package cn.zhumingwu.data.hub.admin.enums;

import cn.zhumingwu.base.support.CommonEnum;

public enum ScheduleType implements CommonEnum {
    None(0, "None"),
    CRON(1, "Cron"),
    FIXED(2, "Fixed"),

    ONCE(2, "Once");
    private final int code;
    private final String name;

    ScheduleType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.name;
    }
}
