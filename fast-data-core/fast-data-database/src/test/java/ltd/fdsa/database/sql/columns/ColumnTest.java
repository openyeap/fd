package ltd.fdsa.database.sql.columns;

import ltd.fdsa.database.sql.columns.string.VarCharColumnBuilder;
import ltd.fdsa.database.sql.conditions.GenericCondition;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.testsupport.ColumnTestSupport;
import org.junit.jupiter.api.Test;

import static ltd.fdsa.database.sql.conditions.GenericCondition.GenericConditionType.IS_BETWEEN;
import static ltd.fdsa.database.sql.domain.Placeholder.placeholder;

public abstract class ColumnTest<C extends Column, B extends ColumnBuilder<C, B, V>, V> extends ColumnTestSupport<C, B, V>
{
    @SuppressWarnings("unchecked")
    @Override
    protected B getColumnBuilder(Table table, String name)
    {
        return (B) new VarCharColumnBuilder(table, name);
    }

    @Test
    void testColumnConditions()
    {
        assertCondition(column -> column.isEqualTo(getOtherColumn())).isEqualTo("= `table`.`other`");
        assertCondition(column -> column.eq(getOtherColumn())).isEqualTo("= `table`.`other`");
        assertCondition(column -> column.isNotEqualTo(getOtherColumn())).isEqualTo("!= `table`.`other`");
        assertCondition(column -> column.nEq(getOtherColumn())).isEqualTo("!= `table`.`other`");
        assertCondition(column -> column.isNull()).isEqualTo("IS NULL");
        assertCondition(column -> column.isNotNull()).isEqualTo("IS NOT NULL");
    }

    @Test
    void testResolvePlaceholdersWithOwnCondition()
    {
        assertCondition(column -> new MyCondition(IS_BETWEEN, placeholder("anyColumnOfSpecialSize"), "hello", "world"))
                .isEqualTo("BETWEEN 'hello' AND 'world'");
    }

    private class MyCondition extends GenericCondition
    {
        public MyCondition(GenericConditionType type, Object... values)
        {
            super(type, values);
        }
    }
}
