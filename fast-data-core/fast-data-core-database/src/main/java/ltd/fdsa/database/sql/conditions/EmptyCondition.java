package ltd.fdsa.database.sql.conditions;

import ltd.fdsa.database.sql.domain.BuildingContext;
import ltd.fdsa.database.sql.utils.Indentation;
/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:39 AM
 */
public class EmptyCondition extends Condition
{
    @Override
    protected String doBuild(BuildingContext context, Indentation indentation)
    {
        throw new UnsupportedOperationException("the EmptyCondition is not meant to be build");
    }
}
