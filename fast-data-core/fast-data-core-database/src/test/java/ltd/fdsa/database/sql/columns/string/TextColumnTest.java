package ltd.fdsa.database.sql.columns.string;

import java.util.function.BiFunction;

import ltd.fdsa.database.sql.testsupport.ColumnAliasTestSupport;
import ltd.fdsa.database.sql.testsupport.Consumers;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.schema.Table;

class TextColumnTest extends StringColumnTest<TextColumn, TextColumnBuilder> implements ColumnAliasTestSupport<TextColumn, TextColumnBuilder, CharSequence>
{
    @Override
    public TextColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new TextColumnBuilder(table, name);
    }

    @Override
    public BiFunction<TextColumn, String, TextColumn> getAliasFunction()
    {
        return (column, alias) -> column.as(alias);
    }

    @Test
    void testTextColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("TEXT DEFAULT NULL");
        assertBuild(builder -> builder.notNull()).isEqualTo("TEXT NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("TEXT DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("TEXT");
        assertBuild(builder -> builder.defaultValue("anyDefaultValue")).isEqualTo("TEXT DEFAULT 'anyDefaultValue'");
        assertBuild(builder -> builder.notNull().defaultValue("anyDefaultValue")).isEqualTo("TEXT NOT NULL DEFAULT 'anyDefaultValue'");
    }
}
