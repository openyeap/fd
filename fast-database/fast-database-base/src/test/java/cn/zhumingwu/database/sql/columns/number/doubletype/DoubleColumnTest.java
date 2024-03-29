package cn.zhumingwu.database.sql.columns.number.doubletype;

import cn.zhumingwu.database.sql.testsupport.ColumnAliasTestSupport;
import cn.zhumingwu.database.sql.testsupport.Consumers;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

class DoubleColumnTest extends DoubleTypeColumnTest<DoubleColumn, DoubleColumnBuilder>
        implements ColumnAliasTestSupport<DoubleColumn, DoubleColumnBuilder, Double>
{
    @Override
    public DoubleColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new DoubleColumnBuilder(table, name);
    }

    @Override
    public BiFunction<DoubleColumn, String, DoubleColumn> getAliasFunction()
    {
        return (column, alias) -> (DoubleColumn) column.as(alias);
    }

    @Test
    void testDecimalColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("DOUBLE DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("DOUBLE NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("DOUBLE DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("DOUBLE");
        assertBuild(builder -> builder.size(3.3).defaultNull()).isEqualTo("DOUBLE(3,3) DEFAULT NULL");
        assertBuild(builder -> builder.size(3.3).defaultValue(123.123)).isEqualTo("DOUBLE(3,3) DEFAULT 123.123");
        assertBuild(builder -> builder.size(Double.valueOf(3.3)).defaultValue(Double.valueOf(123.123))).isEqualTo("DOUBLE(3,3) DEFAULT 123.123");
        assertBuild(builder -> builder.size(3.3).nullable(false).defaultValue(123.123)).isEqualTo("DOUBLE(3,3) NOT NULL DEFAULT 123.123");
    }
}
