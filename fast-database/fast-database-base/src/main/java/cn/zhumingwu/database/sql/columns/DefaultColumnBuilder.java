package cn.zhumingwu.database.sql.columns;

import cn.zhumingwu.database.sql.columns.string.StringColumnBuilder;
import cn.zhumingwu.database.sql.domain.IntSize;
import cn.zhumingwu.database.sql.schema.Table;

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
        return new DefaultColumn(table, name, this.alias, new ColumnDefinition(type, size, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
