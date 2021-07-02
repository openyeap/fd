package ltd.fdsa.database.sql.columns.number.integer;

import static java.sql.Types.INTEGER;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class MediumIntColumn extends IntegerColumn<MediumIntColumn>
{
    public MediumIntColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, INTEGER);
    }

    public MediumIntColumn as(String alias)
    {
        return new MediumIntColumn(table, name, alias, columnDefinition);
    }
}
