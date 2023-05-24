package cn.zhumingwu.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class PlainGroupable implements Groupable {
    private final String value;

    @Override
    public String getValue(BuildingContext context, Indentation indentation) {
        return value;
    }
}
