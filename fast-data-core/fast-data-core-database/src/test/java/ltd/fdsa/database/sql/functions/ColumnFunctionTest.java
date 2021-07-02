package ltd.fdsa.database.sql.functions;

import static ltd.fdsa.database.sql.functions.Function.count;

import ltd.fdsa.database.sql.testsupport.FunctionConditionTestSupport;
import org.junit.jupiter.api.Test;

class ColumnFunctionTest extends FunctionConditionTestSupport
{
    @Test
    void testColumnFunctionConditions()
    {
        assertCondition(() -> Function.count().isEqualTo(getOtherColumn())).isEqualTo("COUNT(*) = `table`.`other`");
        assertCondition(() -> Function.count().eq(getOtherColumn())).isEqualTo("COUNT(*) = `table`.`other`");
        assertCondition(() -> Function.count().isNotEqualTo(getOtherColumn())).isEqualTo("COUNT(*) != `table`.`other`");
        assertCondition(() -> Function.count().nEq(getOtherColumn())).isEqualTo("COUNT(*) != `table`.`other`");
    }
}
