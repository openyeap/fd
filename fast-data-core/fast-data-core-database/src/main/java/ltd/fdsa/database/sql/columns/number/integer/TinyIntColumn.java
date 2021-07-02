package ltd.fdsa.database.sql.columns.number.integer;

import static java.sql.Types.TINYINT;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class TinyIntColumn extends IntegerColumn<TinyIntColumn>
{
    public TinyIntColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, TINYINT);
    }

    public TinyIntColumn as(String alias)
    {
        return new TinyIntColumn(table, name, alias, columnDefinition);
    }
}
