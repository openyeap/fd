package ltd.fdsa.database.sql.columns.string;

import static java.sql.Types.VARCHAR;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class VarCharColumn extends StringColumn<VarCharColumn>
{
    VarCharColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, VARCHAR);
    }

    public VarCharColumn as(String alias)
    {
        return new VarCharColumn(getTable(), getName(), alias, columnDefinition);
    }
}
