package ltd.fdsa.database.sql.columns;

import ltd.fdsa.database.sql.columns.string.StringColumnBuilder;
import ltd.fdsa.database.sql.domain.IntSize;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
public class DefaultColumnBuilder extends StringColumnBuilder<DefaultColumnBuilder, DefaultColumn> {
    private IntSize size;
    private String type = "Unknown";

    public DefaultColumnBuilder(Table table, String name) {
        super(table, name);
    }

    public DefaultColumnBuilder size(int value) {
        size = IntSize.valueOf(Integer.valueOf(value));
        return this;
    }

    public DefaultColumnBuilder size(Integer value) {
        size = IntSize.valueOf(value);
        return this;
    }

    public DefaultColumnBuilder type(String value) {
        this.type = value;
        return this;
    }

    @Override
    protected DefaultColumn getColumnInstance() {
        return new DefaultColumn(table, name, null, new ColumnDefinition(type, size, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
