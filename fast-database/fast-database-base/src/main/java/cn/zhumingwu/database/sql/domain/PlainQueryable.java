package cn.zhumingwu.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class PlainQueryable implements Queryable {
    private String value;

    @Getter
    private String alias;

    @Override
    public String getValue(BuildingContext context, Indentation indentation) {
        return value;
    }

    public Queryable as(String alias) {
        return new PlainQueryable(value, alias);
    }
}
