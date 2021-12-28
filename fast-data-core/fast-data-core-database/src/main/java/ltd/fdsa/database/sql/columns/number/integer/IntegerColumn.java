package ltd.fdsa.database.sql.columns.number.integer;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.columns.number.NumberColumn;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public abstract class IntegerColumn<T extends IntegerColumn<T>> extends NumberColumn<T, Integer> {
    public IntegerColumn(Table table, String name, String alias, ColumnDefinition columnDefinition, int sqlType) {
        super(table, name, alias, columnDefinition, sqlType);
    }
}
