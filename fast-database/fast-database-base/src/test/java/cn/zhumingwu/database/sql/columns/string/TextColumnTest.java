package cn.zhumingwu.database.sql.columns.string;

import cn.zhumingwu.database.sql.testsupport.ColumnAliasTestSupport;
import cn.zhumingwu.database.sql.testsupport.Consumers;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

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
        return (column, alias) -> (TextColumn) column.as(alias);
    }

    @Test
    void testTextColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("TEXT DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("TEXT NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("TEXT DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("TEXT");
        assertBuild(builder -> builder.defaultValue("anyDefaultValue")).isEqualTo("TEXT DEFAULT 'anyDefaultValue'");
        assertBuild(builder -> builder.nullable(false).defaultValue("anyDefaultValue")).isEqualTo("TEXT NOT NULL DEFAULT 'anyDefaultValue'");
    }
}
