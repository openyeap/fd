package cn.zhumingwu.database.sql.columns.string;

import cn.zhumingwu.database.sql.columns.Column;
import cn.zhumingwu.database.sql.columns.ColumnBuilder;
import cn.zhumingwu.database.sql.schema.Table;

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
