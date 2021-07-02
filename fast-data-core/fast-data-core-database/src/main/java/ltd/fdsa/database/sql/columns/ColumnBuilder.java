package ltd.fdsa.database.sql.columns;

import lombok.RequiredArgsConstructor;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.schema.TableBuilderUtil;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:36 AM
 */
@RequiredArgsConstructor
public abstract class ColumnBuilder<C extends Column, B extends ColumnBuilder<C, B, V>, V> {
    protected final Table table;
    protected final String name;

    protected boolean isNullable = true;
    protected boolean isDefaultNull = true;
    protected V defaultValue;

    protected abstract C getColumnInstance();

    public C build() {
        C columnInstance = getColumnInstance();
        TableBuilderUtil.addColumnToTable(columnInstance, table);
        return columnInstance;
    }

    @SuppressWarnings("unchecked")
    public B nullable(boolean nullable) {
        isNullable = nullable;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B defaultValue(V value) {
        isDefaultNull = false;
        defaultValue = value;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B defaultNull() {
        isNullable = true;
        defaultValue = null;
        isDefaultNull = true;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B noDefault() {
        defaultValue = null;
        isDefaultNull = false;
        return (B) this;
    }

    public B nullable() {
        return nullable(true);
    }

    public B notNull() {
        return nullable(false);
    }
}
