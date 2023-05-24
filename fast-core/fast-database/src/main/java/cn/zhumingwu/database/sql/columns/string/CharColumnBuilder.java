package cn.zhumingwu.database.sql.columns.string;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.domain.IntSize;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class CharColumnBuilder extends StringColumnBuilder<CharColumnBuilder, CharColumn>
{
    private IntSize size;

    public CharColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    public CharColumnBuilder size(int value)
    {
        size = IntSize.valueOf(Integer.valueOf(value));
        return this;
    }

    public CharColumnBuilder size(Integer value)
    {
        size = IntSize.valueOf(value);
        return this;
    }

    @Override
    protected CharColumn getColumnInstance()
    {
        return new CharColumn(table, name, null, new ColumnDefinition("CHAR", size, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
