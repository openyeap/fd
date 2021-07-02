package ltd.fdsa.database.sql.columns.number.integer;

import ltd.fdsa.database.sql.columns.number.NumberColumnBuilder;
import ltd.fdsa.database.sql.domain.IntSize;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public abstract class IntegerColumnBuilder<T extends IntegerColumnBuilder<T, C>, C extends IntegerColumn<C>>
        extends NumberColumnBuilder<T, Integer, C>
{
    protected boolean isAutoIncrement;
    protected IntSize size;

    public IntegerColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @SuppressWarnings("unchecked")
    public T size(Integer value)
    {
        this.size = IntSize.valueOf(value);
        return (T) this;
    }

    public T size(int value)
    {
        return size(Integer.valueOf(value));
    }

    public T defaultValue(int value)
    {
        return defaultValue(Integer.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public T autoIncrement(boolean autoIncrement)
    {
        isAutoIncrement = autoIncrement;
        return (T) this;
    }

    public T autoIncrement()
    {
        return autoIncrement(true);
    }
}
