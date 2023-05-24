package cn.zhumingwu.database.sql.columns.number.integer;

import cn.zhumingwu.database.sql.testsupport.ColumnAliasTestSupport;
import cn.zhumingwu.database.sql.testsupport.Consumers;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

class IntColumnTest extends IntegerColumnTest<IntColumn, IntColumnBuilder> implements ColumnAliasTestSupport<IntColumn, IntColumnBuilder, Integer>
{
    @Override
    public IntColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new IntColumnBuilder(table, name);
    }

    @Override
    public BiFunction<IntColumn, String, IntColumn> getAliasFunction()
    {
        return (column, alias) -> (IntColumn) column.as(alias);
    }

    @Test
    void testDecimalColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("INT DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("INT NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("INT DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("INT");
        assertBuild(builder -> builder.noDefault().autoIncrement()).isEqualTo("INT AUTO_INCREMENT");
        assertBuild(builder -> builder.size(5).defaultNull()).isEqualTo("INT(5) DEFAULT NULL");
        assertBuild(builder -> builder.size(5).defaultValue(123)).isEqualTo("INT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(Integer.valueOf(5)).defaultValue(Integer.valueOf(123))).isEqualTo("INT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(5).nullable(false).defaultValue(123)).isEqualTo("INT(5) NOT NULL DEFAULT 123");
    }
}
