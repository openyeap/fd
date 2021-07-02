package ltd.fdsa.database.sql.columns.number;

import ltd.fdsa.database.sql.conditions.NumberConditions;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public abstract class NumberColumn<T extends NumberColumn<T, N>, N extends Number> extends Column implements NumberConditions
{
    public NumberColumn(Table table, String name, String alias, ColumnDefinition columnDefinition, int sqlType)
    {
        super(table, name, alias, columnDefinition, sqlType);
    }
}
