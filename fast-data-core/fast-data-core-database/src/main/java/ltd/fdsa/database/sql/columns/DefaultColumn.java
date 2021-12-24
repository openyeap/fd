package ltd.fdsa.database.sql.columns;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.string.StringColumn;
import ltd.fdsa.database.sql.schema.Table;

import static java.sql.Types.CHAR;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class DefaultColumn extends StringColumn<DefaultColumn> {
    DefaultColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, CHAR);
    }

    public DefaultColumn as(String alias) {
        return new DefaultColumn(getTable(), getName(), alias, columnDefinition);
    }
}
