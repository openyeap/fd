package ltd.fdsa.database.sql.columns.number.doubletype;

import static java.sql.Types.DECIMAL;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class DecimalColumn extends DoubleTypeColumn<DecimalColumn>
{
    public DecimalColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, DECIMAL);
    }

    public DecimalColumn as(String alias)
    {
        return new DecimalColumn(table, name, alias, columnDefinition);
    }
}
