package ltd.fdsa.database.sql.columns.number.integer;

import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.testsupport.ColumnAliasTestSupport;
import ltd.fdsa.database.sql.testsupport.Consumers;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

class BigIntColumnTest extends IntegerColumnTest<BigIntColumn, BigIntColumnBuilder>
        implements ColumnAliasTestSupport<BigIntColumn, BigIntColumnBuilder, Integer>
{
    @Override
    public BigIntColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new BigIntColumnBuilder(table, name);
    }

    @Override
    public BiFunction<BigIntColumn, String, BigIntColumn> getAliasFunction()
    {
        return (column, alias) -> (BigIntColumn) column.as(alias);
    }

    @Test
    void testDecimalColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("BIGINT DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("BIGINT NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("BIGINT DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("BIGINT");
        assertBuild(builder -> builder.noDefault().autoIncrement()).isEqualTo("BIGINT AUTO_INCREMENT");
        assertBuild(builder -> builder.size(5).defaultNull()).isEqualTo("BIGINT(5) DEFAULT NULL");
        assertBuild(builder -> builder.size(5).defaultValue(123)).isEqualTo("BIGINT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(Integer.valueOf(5)).defaultValue(Integer.valueOf(123))).isEqualTo("BIGINT(5) DEFAULT 123");
        assertBuild(builder -> builder.size(5).nullable(false).defaultValue(123)).isEqualTo("BIGINT(5) NOT NULL DEFAULT 123");
    }
}
