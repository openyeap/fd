package ltd.fdsa.database.sql.columns.number.doubletype;

import static java.sql.Types.DOUBLE;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class DoubleColumn extends DoubleTypeColumn<DoubleColumn>
{
    public DoubleColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, DOUBLE);
    }

    public DoubleColumn as(String alias)
    {
        return new DoubleColumn(table, name, alias, columnDefinition);
    }
}
