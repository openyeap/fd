package cn.zhumingwu.database.sql.columns.number.integer;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class TinyIntColumnBuilder extends IntegerColumnBuilder<TinyIntColumnBuilder, TinyIntColumn>
{
    public TinyIntColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected TinyIntColumn getColumnInstance()
    {
        return new TinyIntColumn(table, name, null, new ColumnDefinition("TINYINT", size, isNullable, isDefaultNull, isUnsigned, isAutoIncrement, defaultValue));
    }
}
