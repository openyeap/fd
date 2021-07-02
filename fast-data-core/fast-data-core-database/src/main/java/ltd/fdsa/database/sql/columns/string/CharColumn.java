package ltd.fdsa.database.sql.columns.string;

import static java.sql.Types.CHAR;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;
import lombok.ToString;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class CharColumn extends StringColumn<CharColumn>
{
    CharColumn(Table table, String name, String alias, ColumnDefinition columnDefinition)
    {
        super(table, name, alias, columnDefinition, CHAR);
    }

    public CharColumn as(String alias)
    {
        return new CharColumn(getTable(), getName(), alias, columnDefinition);
    }
}
