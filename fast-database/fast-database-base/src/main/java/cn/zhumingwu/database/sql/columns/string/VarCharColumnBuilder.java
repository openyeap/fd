package cn.zhumingwu.database.sql.columns.string;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.domain.IntSize;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class VarCharColumnBuilder extends StringColumnBuilder<VarCharColumnBuilder, VarCharColumn>
{
    private IntSize size;

    public VarCharColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    public VarCharColumnBuilder size(int value)
    {
        size = IntSize.valueOf(Integer.valueOf(value));
        return this;
    }

    public VarCharColumnBuilder size(Integer value)
    {
        size = IntSize.valueOf(value);
        return this;
    }

    @Override
    protected VarCharColumn getColumnInstance()
    {
        return new VarCharColumn(table, name, null, new ColumnDefinition("VARCHAR", size, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
