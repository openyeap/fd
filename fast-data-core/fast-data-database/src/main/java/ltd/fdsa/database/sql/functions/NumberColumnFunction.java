package ltd.fdsa.database.sql.functions;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.conditions.NumberConditions;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public abstract class NumberColumnFunction extends ColumnFunction implements NumberConditions
{
    public NumberColumnFunction(Column column, String definition)
    {
        super(column, definition);
    }

    public NumberColumnFunction(Column column, String definition, String alias)
    {
        super(column, definition, alias);
    }
}
