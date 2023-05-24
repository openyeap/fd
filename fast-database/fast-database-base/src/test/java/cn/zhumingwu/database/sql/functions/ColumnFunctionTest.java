package cn.zhumingwu.database.sql.functions;

import cn.zhumingwu.database.sql.testsupport.FunctionConditionTestSupport;
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
