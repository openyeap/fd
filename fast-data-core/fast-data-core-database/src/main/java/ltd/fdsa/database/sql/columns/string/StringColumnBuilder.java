package ltd.fdsa.database.sql.columns.string;

import ltd.fdsa.database.sql.columns.Column;
import ltd.fdsa.database.sql.columns.ColumnBuilder;
import ltd.fdsa.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public abstract class StringColumnBuilder<T extends StringColumnBuilder<T, C>, C extends Column> extends ColumnBuilder<C, T, CharSequence>
{
    public StringColumnBuilder(Table table, String name)
    {
        super(table, name);
    }
}
