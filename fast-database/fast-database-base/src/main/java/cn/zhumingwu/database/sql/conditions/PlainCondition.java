package cn.zhumingwu.database.sql.conditions;

import lombok.AllArgsConstructor;
import lombok.ToString;
import cn.zhumingwu.database.sql.domain.BuildingContext;
import cn.zhumingwu.database.sql.utils.Indentation;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@AllArgsConstructor
@ToString(callSuper = true)
public class PlainCondition extends Condition {
    private String value;

    @Override
    protected String doBuild(BuildingContext context, Indentation indentation) {
        return value;
    }
}
