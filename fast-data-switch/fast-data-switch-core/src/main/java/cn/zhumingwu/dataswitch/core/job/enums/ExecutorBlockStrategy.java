package cn.zhumingwu.dataswitch.core.job.enums;


import cn.zhumingwu.base.support.CommonEnum;
import com.google.common.base.Strings;

public enum ExecutorBlockStrategy implements CommonEnum {
    SERIAL(1, "Serial execution"),
    /*CONCURRENT_EXECUTION("并行"),*/
    DISCARD(2, "Discard Later"),
    COVER(3, "Cover Early");
    private final int code;
    private final String name;

    private ExecutorBlockStrategy(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ExecutorBlockStrategy match(String name, ExecutorBlockStrategy defaultItem) {
        if (Strings.isNullOrEmpty(name)) {
            return defaultItem;
        }
        for (ExecutorBlockStrategy item : ExecutorBlockStrategy.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.name;
    }
}
