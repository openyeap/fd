package cn.zhumingwu.database.sql.columns.number.integer;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class SmallIntColumnBuilder extends IntegerColumnBuilder<SmallIntColumnBuilder, SmallIntColumn>
{
    public SmallIntColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected SmallIntColumn getColumnInstance()
    {
        return new SmallIntColumn(table, name, null, new ColumnDefinition("SMALLINT", size, isNullable, isDefaultNull, isUnsigned, isAutoIncrement, defaultValue));
    }
}
