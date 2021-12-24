package ltd.fdsa.database.sql.columns.string;

import lombok.ToString;
import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.conditions.StringConditions;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@ToString(callSuper = true)
public abstract class StringColumn<T extends StringColumn<T>> extends Column implements StringConditions {
    public StringColumn(Table table, String name, String alias, ColumnDefinition columnDefinition, int sqlType) {
        super(table, name, alias, columnDefinition, sqlType);
    }
}
