package ltd.fdsa.database.sql.columns.string;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

import static java.sql.Types.CHAR;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class CharColumn extends StringColumn<CharColumn> {
    CharColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, CHAR);
    }
}
