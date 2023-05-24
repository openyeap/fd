package cn.zhumingwu.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class IntSize implements Size {
    private final int value;

    public static IntSize valueOf(Integer size) {
        return size != null ? new IntSize(size.intValue()) : null;
    }

    @Override
    public String getValue() {
        return Integer.toString(value);
    }
}
