package cn.zhumingwu.dataswitch.core.job.enums;

import cn.zhumingwu.base.support.CommonEnum;

public enum JobStatus implements CommonEnum {
    SUCCESS(0),
    FAILED(-1);

    private final int code;

    JobStatus(int status) {
        this.code = status;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.name();
    }
}
