package cn.zhumingwu.database.sql.columns.number.integer;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class IntColumnBuilder extends IntegerColumnBuilder<IntColumnBuilder, IntColumn>
{
    public IntColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected IntColumn getColumnInstance()
    {
        return new IntColumn(table, name, null, new ColumnDefinition("INT", size, isNullable, isDefaultNull, isUnsigned, isAutoIncrement, defaultValue));
    }
}
