package ltd.fdsa.database.sql.columns.number.doubletype;

import static java.sql.Types.FLOAT;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class FloatColumn extends DoubleTypeColumn<FloatColumn>
{
    public FloatColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, FLOAT);
    }

    public FloatColumn as(String alias)
    {
        return new FloatColumn(getTable(), getName(), alias, columnDefinition);
    }
}
