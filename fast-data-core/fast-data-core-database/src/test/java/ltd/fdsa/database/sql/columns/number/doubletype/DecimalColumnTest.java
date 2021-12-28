package ltd.fdsa.database.sql.columns.number.doubletype;

import java.util.function.BiFunction;

import ltd.fdsa.database.sql.testsupport.ColumnAliasTestSupport;
import ltd.fdsa.database.sql.testsupport.Consumers;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.schema.Table;

class DecimalColumnTest extends DoubleTypeColumnTest<DecimalColumn, DecimalColumnBuilder>
        implements ColumnAliasTestSupport<DecimalColumn, DecimalColumnBuilder, Double>
{
    @Override
    public DecimalColumnBuilder getColumnBuilder(Table table, String name)
    {
        return new DecimalColumnBuilder(table, name);
    }

    @Override
    public BiFunction<DecimalColumn, String, DecimalColumn> getAliasFunction()
    {
        return (column, alias) -> (DecimalColumn) column.as(alias);
    }

    @Test
    void testDecimalColumnDefinition()
    {
        assertBuild(Consumers::noAction).isEqualTo("DECIMAL DEFAULT NULL");
        assertBuild(builder -> builder.nullable(false)).isEqualTo("DECIMAL NOT NULL");
        assertBuild(builder -> builder.defaultNull()).isEqualTo("DECIMAL DEFAULT NULL");
        assertBuild(builder -> builder.noDefault()).isEqualTo("DECIMAL");
        assertBuild(builder -> builder.size(3.3).defaultNull()).isEqualTo("DECIMAL(3,3) DEFAULT NULL");
        assertBuild(builder -> builder.size(3.3).defaultValue(123.123)).isEqualTo("DECIMAL(3,3) DEFAULT 123.123");
        assertBuild(builder -> builder.size(Double.valueOf(3.3)).defaultValue(Double.valueOf(123.123))).isEqualTo("DECIMAL(3,3) DEFAULT 123.123");
        assertBuild(builder -> builder.size(3.3).nullable(false).defaultValue(123.123)).isEqualTo("DECIMAL(3,3) NOT NULL DEFAULT 123.123");
    }
}
