package cn.zhumingwu.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@Getter
@ToString
public enum JoinType {
    LEFT("LEFT"),
    LEFT_OUTER("LEFT OUTER"),
    RIGHT("RIGHT"),
    RIGHT_OUTER("RIGHT OUTER"),
    INNER("INNER"),
    OUTER("OUTER"),
    FULL_OUTER("FULL OUTER");

    private final String value;
}
