package cn.zhumingwu.database.sql.columns;

import lombok.RequiredArgsConstructor;
import cn.zhumingwu.database.sql.schema.Table;
import cn.zhumingwu.database.sql.schema.TableBuilderUtil;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@RequiredArgsConstructor
public abstract class ColumnBuilder<C extends Column, B extends ColumnBuilder<C, B, V>, V> {
    protected final Table table;
    protected final String name;

    protected String alias;
    protected boolean isNullable = true;
    protected boolean isDefaultNull = true;
    protected V defaultValue;

    protected abstract C getColumnInstance();

    public C build() {
        C columnInstance = getColumnInstance();
        TableBuilderUtil.addColumnToTable(columnInstance, table);
        return columnInstance;
    }

    public B nullable(boolean nullable) {
        isNullable = nullable;
        return (B) this;
    }

    public B alias(String alias) {
        this.alias = alias;
        return (B) this;
    }

    public B nullable() {
        return nullable(true);
    }

    public B defaultValue(V value) {
        isDefaultNull = false;
        defaultValue = value;
        return (B) this;
    }

    public B defaultNull() {
        isNullable = true;
        defaultValue = null;
        isDefaultNull = true;
        return (B) this;
    }

    public B noDefault() {
        defaultValue = null;
        isDefaultNull = false;
        return (B) this;
    }
}
