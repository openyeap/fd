package cn.zhumingwu.database.sql.functions;

import cn.zhumingwu.database.sql.conditions.NumberConditions;
import lombok.ToString;
import cn.zhumingwu.database.sql.columns.Column;

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
