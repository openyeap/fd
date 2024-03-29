package cn.zhumingwu.database.sql.columns.number.doubletype;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class DoubleColumnBuilder extends DoubleTypeColumnBuilder<DoubleColumnBuilder, DoubleColumn>
{
    public DoubleColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected DoubleColumn getColumnInstance()
    {
        return new DoubleColumn(table, name, null, new ColumnDefinition("DOUBLE", size, isNullable, isDefaultNull, isUnsigned, false, defaultValue));
    }
}
