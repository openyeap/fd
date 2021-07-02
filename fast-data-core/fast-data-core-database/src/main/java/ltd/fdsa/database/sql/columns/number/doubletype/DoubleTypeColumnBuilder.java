package ltd.fdsa.database.sql.columns.number.doubletype;

import ltd.fdsa.database.sql.columns.number.NumberColumn;
import ltd.fdsa.database.sql.columns.number.NumberColumnBuilder;
import ltd.fdsa.database.sql.domain.DoubleSize;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public abstract class DoubleTypeColumnBuilder<T extends DoubleTypeColumnBuilder<T, C>, C extends NumberColumn<C, Double>>
        extends NumberColumnBuilder<T, Double, C>
{
    protected DoubleSize size;

    public DoubleTypeColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    public T defaultValue(double value)
    {
        return defaultValue(Double.valueOf(value));
    }

    @SuppressWarnings("unchecked")
    public T size(Double value)
    {
        this.size = DoubleSize.valueOf(value);
        return (T) this;
    }

    public T size(double value)
    {
        return size(Double.valueOf(value));
    }
}
