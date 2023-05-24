package cn.zhumingwu.database.sql.columns.string;

import cn.zhumingwu.database.sql.columns.ColumnDefinition;
import cn.zhumingwu.database.sql.schema.Table;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public class TextColumnBuilder extends StringColumnBuilder<TextColumnBuilder, TextColumn>
{
    public TextColumnBuilder(Table table, String name)
    {
        super(table, name);
    }

    @Override
    protected TextColumn getColumnInstance()
    {
        return new TextColumn(table, name, null, new ColumnDefinition("TEXT", null, isNullable, isDefaultNull, false, false, defaultValue));
    }
}
