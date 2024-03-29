package cn.zhumingwu.database.sql.columns.string;

import cn.zhumingwu.database.sql.testsupport.ColumnAliasTestSupport;
import cn.zhumingwu.database.sql.testsupport.Consumers;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

class CharColumnTest extends StringColumnTest<CharColumn, CharColumnBuilder> implements ColumnAliasTestSupport<CharColumn, CharColumnBuilder, CharSequence>
{
    @Override
    public CharColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new CharColumnBuilder(table, name);
    }

    @Override
    public BiFunction<CharColumn, String, CharColumn> getAliasFunction()
    {
        return (column, alias) -> (CharColumn) column.as(alias);
    }

    @Test
    void testCharColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("CHAR DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("CHAR NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("CHAR DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("CHAR");
        assertBuild(builder -> builder.size(10).defaultNull()).isEqualTo("CHAR(10) DEFAULT NULL");
        assertBuild(builder -> builder.size(Integer.valueOf(10)).defaultValue("anyDefaultValue")).isEqualTo("CHAR(10) DEFAULT 'anyDefaultValue'");
        assertBuild(builder -> builder.size(10).nullable(false).defaultValue("anyDefaultValue")).isEqualTo("CHAR(10) NOT NULL DEFAULT 'anyDefaultValue'");
    }
}
