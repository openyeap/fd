package cn.zhumingwu.database.sql.columns.number.integer;

import cn.zhumingwu.database.sql.testsupport.ColumnAliasTestSupport;
import cn.zhumingwu.database.sql.testsupport.Consumers;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

class SmallIntColumnTest extends IntegerColumnTest<SmallIntColumn, SmallIntColumnBuilder>
        implements ColumnAliasTestSupport<SmallIntColumn, SmallIntColumnBuilder, Integer>
{
    @Override
    public SmallIntColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new SmallIntColumnBuilder(table, name);
    }

    @Override
    public BiFunction<SmallIntColumn, String, SmallIntColumn> getAliasFunction()
    {
        return (column, alias) -> (SmallIntColumn) column.as(alias);
    }

    @Test
    void testDecimalColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("SMALLINT DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("SMALLINT NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("SMALLINT DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("SMALLINT");
        assertBuild(builder -> builder.noDefault().autoIncrement()).isEqualTo("SMALLINT AUTO_INCREMENT");
        assertBuild(builder -> builder.size(5).defaultNull()).isEqualTo("SMALLINT(5) DEFAULT NULL");
        assertBuild(builder -> builder.size(5).defaultValue(123)).isEqualTo("SMALLINT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(Integer.valueOf(5)).defaultValue(Integer.valueOf(123))).isEqualTo("SMALLINT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(5).nullable(false).defaultValue(123)).isEqualTo("SMALLINT(5) NOT NULL DEFAULT 123");
    }
}
