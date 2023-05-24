package cn.zhumingwu.database.sql.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import cn.zhumingwu.database.sql.utils.BuilderUtils;
import cn.zhumingwu.database.sql.utils.Indentation;

import static lombok.AccessLevel.PACKAGE;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString
public class PlainValuable implements Valuable {
    @Getter(PACKAGE)
    protected Object value;

    @Override
    public String getValue(BuildingContext context, Indentation indentation) {
        return BuilderUtils.getValued(value, context, indentation);
    }
}
