package ltd.fdsa.database.sql.columns.string;

import ltd.fdsa.database.sql.columns.ColumnDefinition;
import ltd.fdsa.database.sql.schema.Table;

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
