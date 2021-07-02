package ltd.fdsa.database.sql.conditions;

import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.utils.Indentation;
import lombok.AllArgsConstructor;
import lombok.ToString;

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
