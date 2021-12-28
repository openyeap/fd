package ltd.fdsa.database.sql.columns.number.integer;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

import static java.sql.Types.SMALLINT;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public class SmallIntColumn extends IntegerColumn<SmallIntColumn> {
    public SmallIntColumn(Table table, String name, String alias, ColumnDefinition columnDefinition) {
        super(table, name, alias, columnDefinition, SMALLINT);
    }
}
