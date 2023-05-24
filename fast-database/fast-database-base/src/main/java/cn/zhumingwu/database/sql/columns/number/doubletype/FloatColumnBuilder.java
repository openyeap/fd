package cn.zhumingwu.database.sql.columns.number.doubletype;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class FloatColumnBuilder extends DoubleTypeColumnBuilder<FloatColumnBuilder, FloatColumn>
{
    public FloatColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected FloatColumn getColumnInstance()
    {
        return new FloatColumn(table, name, null, new ColumnDefinition("FLOAT", size, isNullable, isDefaultNull, isUnsigned, false, defaultValue));
    }
}
