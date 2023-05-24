package cn.zhumingwu.starter.register.model;

import com.google.common.base.Strings;
import cn.zhumingwu.base.support.CommonEnum;

import java.util.Arrays;

public enum StatusType implements CommonEnum {
    UP(100, "UP"),
    DOWN(-1, "DOWN"),
    STARTING(1, "STARTING"),
    OUT_OF_SERVICE(-2, "OUT_OF_SERVICE"),
    UNKNOWN(0, "UNKNOWN");

    private final int code;
    private final String name;


    private StatusType(int code, String name) {
        this.name = name;
        this.code = code;
    }


    public static StatusType get(String name) {
        if (Strings.isNullOrEmpty(name)) return UNKNOWN;
        return Arrays.stream(StatusType.values()).filter(i -> i.name.compareToIgnoreCase(name) == 0).findAny().orElse(UNKNOWN);
    }


    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
