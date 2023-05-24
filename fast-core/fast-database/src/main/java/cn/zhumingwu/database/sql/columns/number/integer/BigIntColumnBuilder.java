package cn.zhumingwu.database.sql.columns.number.integer;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class BigIntColumnBuilder extends IntegerColumnBuilder<BigIntColumnBuilder, BigIntColumn>
{
    public BigIntColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected BigIntColumn getColumnInstance()
    {
        return new BigIntColumn(table, name, null, new ColumnDefinition("BIGINT", size, isNullable, isDefaultNull, isUnsigned, isAutoIncrement, defaultValue));
    }
}
