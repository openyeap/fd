package ltd.fdsa.database.sql.functions;

import static ltd.fdsa.database.sql.functions.Function.count;

import ltd.fdsa.database.sql.testsupport.FunctionAssert;
import ltd.fdsa.database.sql.testsupport.FunctionConditionTestSupport;
import org.junit.jupiter.api.Test;

class FunctionTest extends FunctionConditionTestSupport implements FunctionAssert
{
    @Test
    void testFunctionDefinitions()
    {
        assertFunction(Function.now()).isEqualTo("NOW()");
        assertFunction(Function.count()).isEqualTo("COUNT(*)");
        assertFunction(Function.count(getOtherColumn())).isEqualTo("COUNT(`table`.`other`)");
        assertFunction(Function.avg(getOtherColumn())).isEqualTo("AVG(`table`.`other`)");
        assertFunction(Function.max(getOtherColumn())).isEqualTo("MAX(`table`.`other`)");
        assertFunction(Function.min(getOtherColumn())).isEqualTo("MIN(`table`.`other`)");
        assertFunction(Function.sum(getOtherColumn())).isEqualTo("SUM(`table`.`other`)");
    }
}
