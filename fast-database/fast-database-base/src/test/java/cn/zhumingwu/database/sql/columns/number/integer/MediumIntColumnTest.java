package cn.zhumingwu.database.sql.columns.number.integer;

import cn.zhumingwu.database.sql.testsupport.ColumnAliasTestSupport;
import cn.zhumingwu.database.sql.testsupport.Consumers;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

class MediumIntColumnTest extends IntegerColumnTest<MediumIntColumn, MediumIntColumnBuilder>
        implements ColumnAliasTestSupport<MediumIntColumn, MediumIntColumnBuilder, Integer>
{
    @Override
    public MediumIntColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new MediumIntColumnBuilder(table, name);
    }

    @Override
    public BiFunction<MediumIntColumn, String, MediumIntColumn> getAliasFunction()
    {
        return (column, alias) -> (MediumIntColumn) column.as(alias);
    }

    @Test
    void testDecimalColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("MEDIUMINT DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("MEDIUMINT NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("MEDIUMINT DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("MEDIUMINT");
        assertBuild(builder -> builder.noDefault().autoIncrement()).isEqualTo("MEDIUMINT AUTO_INCREMENT");
        assertBuild(builder -> builder.size(5).defaultNull()).isEqualTo("MEDIUMINT(5) DEFAULT NULL");
        assertBuild(builder -> builder.size(5).defaultValue(123)).isEqualTo("MEDIUMINT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(Integer.valueOf(5)).defaultValue(Integer.valueOf(123))).isEqualTo("MEDIUMINT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(5).nullable(false).defaultValue(123)).isEqualTo("MEDIUMINT(5) NOT NULL DEFAULT 123");
    }
}
