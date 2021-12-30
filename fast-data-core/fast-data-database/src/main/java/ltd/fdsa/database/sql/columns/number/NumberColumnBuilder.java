package ltd.fdsa.database.sql.columns.number;

import ltd.fdsa.database.sql.columns.ColumnBuilder;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public abstract class NumberColumnBuilder<T extends NumberColumnBuilder<T, N, C>, N extends Number, C extends NumberColumn<C, N>> extends ColumnBuilder<C, T, N>
{
    protected boolean isUnsigned;

    public NumberColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @SuppressWarnings("unchecked")
    public T unsigned(boolean unsigned)
    {
        isUnsigned = unsigned;
        return (T) this;
    }

    public T unsigned()
    {
        return unsigned(true);
    }
}
