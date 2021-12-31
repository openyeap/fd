package ltd.fdsa.database.sql.functions;

import ltd.fdsa.database.sql.testsupport.FunctionConditionTestSupport;
import org.junit.jupiter.api.Test;

class NumberColumnFunctionTest extends FunctionConditionTestSupport
{
    @Test
    void testNumberColumnFunctionConditions()
    {
        assertCondition(() -> Function.count().isEqualTo((Integer) null)).isEqualTo("COUNT(*) IS NULL");
        assertCondition(() -> Function.count().eq((Integer) null)).isEqualTo("COUNT(*) IS NULL");
        assertCondition(() -> Function.count().isEqualTo(Integer.valueOf(123))).isEqualTo("COUNT(*) = 123");
        assertCondition(() -> Function.count().eq(Integer.valueOf(123))).isEqualTo("COUNT(*) = 123");
        assertCondition(() -> Function.count().isEqualTo(123)).isEqualTo("COUNT(*) = 123");
        assertCondition(() -> Function.count().eq(123)).isEqualTo("COUNT(*) = 123");
        assertCondition(() -> Function.count().isEqualTo(123.123)).isEqualTo("COUNT(*) = 123.123");
        assertCondition(() -> Function.count().eq(123.123)).isEqualTo("COUNT(*) = 123.123");
        assertCondition(() -> Function.count().isNotEqualTo((Integer) null)).isEqualTo("COUNT(*) IS NOT NULL");
        assertCondition(() -> Function.count().nEq((Integer) null)).isEqualTo("COUNT(*) IS NOT NULL");
        assertCondition(() -> Function.count().isNotEqualTo(Integer.valueOf(123))).isEqualTo("COUNT(*) != 123");
        assertCondition(() -> Function.count().nEq(Integer.valueOf(123))).isEqualTo("COUNT(*) != 123");
        assertCondition(() -> Function.count().isNotEqualTo(123)).isEqualTo("COUNT(*) != 123");
        assertCondition(() -> Function.count().nEq(123)).isEqualTo("COUNT(*) != 123");
        assertCondition(() -> Function.count().isNotEqualTo(123.123)).isEqualTo("COUNT(*) != 123.123");
        assertCondition(() -> Function.count().nEq(123.123)).isEqualTo("COUNT(*) != 123.123");
        assertCondition(() -> Function.count().isGreaterThan(Integer.valueOf(123))).isEqualTo("COUNT(*) > 123");
        assertCondition(() -> Function.count().gt(Integer.valueOf(123))).isEqualTo("COUNT(*) > 123");
        assertCondition(() -> Function.count().isGreaterThan(123)).isEqualTo("COUNT(*) > 123");
        assertCondition(() -> Function.count().gt(123)).isEqualTo("COUNT(*) > 123");
        assertCondition(() -> Function.count().isGreaterThan(123.123)).isEqualTo("COUNT(*) > 123.123");
        assertCondition(() -> Function.count().gt(123.123)).isEqualTo("COUNT(*) > 123.123");
        assertCondition(() -> Function.count().isGreaterThan(getOtherColumn())).isEqualTo("COUNT(*) > `table`.`other`");
        assertCondition(() -> Function.count().gt(getOtherColumn())).isEqualTo("COUNT(*) > `table`.`other`");
        assertCondition(() -> Function.count().isGreaterThanOrEqualTo(Integer.valueOf(123))).isEqualTo("COUNT(*) >= 123");
        assertCondition(() -> Function.count().gtEq(Integer.valueOf(123))).isEqualTo("COUNT(*) >= 123");
        assertCondition(() -> Function.count().isGreaterThanOrEqualTo(123)).isEqualTo("COUNT(*) >= 123");
        assertCondition(() -> Function.count().gtEq(123)).isEqualTo("COUNT(*) >= 123");
        assertCondition(() -> Function.count().isGreaterThanOrEqualTo(123.123)).isEqualTo("COUNT(*) >= 123.123");
        assertCondition(() -> Function.count().gtEq(123.123)).isEqualTo("COUNT(*) >= 123.123");
        assertCondition(() -> Function.count().isGreaterThanOrEqualTo(getOtherColumn())).isEqualTo("COUNT(*) >= `table`.`other`");
        assertCondition(() -> Function.count().gtEq(getOtherColumn())).isEqualTo("COUNT(*) >= `table`.`other`");
        assertCondition(() -> Function.count().isLessThan(Integer.valueOf(123))).isEqualTo("COUNT(*) < 123");
        assertCondition(() -> Function.count().lt(Integer.valueOf(123))).isEqualTo("COUNT(*) < 123");
        assertCondition(() -> Function.count().isLessThan(123)).isEqualTo("COUNT(*) < 123");
        assertCondition(() -> Function.count().lt(123)).isEqualTo("COUNT(*) < 123");
        assertCondition(() -> Function.count().isLessThan(123.123)).isEqualTo("COUNT(*) < 123.123");
        assertCondition(() -> Function.count().lt(123.123)).isEqualTo("COUNT(*) < 123.123");
        assertCondition(() -> Function.count().isLessThan(getOtherColumn())).isEqualTo("COUNT(*) < `table`.`other`");
        assertCondition(() -> Function.count().lt(getOtherColumn())).isEqualTo("COUNT(*) < `table`.`other`");
        assertCondition(() -> Function.count().isLessThanOrEqualTo(Integer.valueOf(123))).isEqualTo("COUNT(*) <= 123");
        assertCondition(() -> Function.count().ltEq(Integer.valueOf(123))).isEqualTo("COUNT(*) <= 123");
        assertCondition(() -> Function.count().isLessThanOrEqualTo(123)).isEqualTo("COUNT(*) <= 123");
        assertCondition(() -> Function.count().ltEq(123)).isEqualTo("COUNT(*) <= 123");
        assertCondition(() -> Function.count().isLessThanOrEqualTo(123.123)).isEqualTo("COUNT(*) <= 123.123");
        assertCondition(() -> Function.count().ltEq(123.123)).isEqualTo("COUNT(*) <= 123.123");
        assertCondition(() -> Function.count().isLessThanOrEqualTo(getOtherColumn())).isEqualTo("COUNT(*) <= `table`.`other`");
        assertCondition(() -> Function.count().ltEq(getOtherColumn())).isEqualTo("COUNT(*) <= `table`.`other`");
        assertCondition(() -> Function.count().isBetween(Integer.valueOf(123), Integer.valueOf(456))).isEqualTo("COUNT(*) BETWEEN 123 AND 456");
        assertCondition(() -> Function.count().isBetween(123, 456)).isEqualTo("COUNT(*) BETWEEN 123 AND 456");
        assertCondition(() -> Function.count().isBetween(123.123, 456.456)).isEqualTo("COUNT(*) BETWEEN 123.123 AND 456.456");
        assertCondition(() -> Function.count().isBetween(getOtherColumn(), getOtherColumn2())).isEqualTo("COUNT(*) BETWEEN `table`.`other` AND `table`.`other2`");
        assertCondition(() -> Function.count().isBetween(getOtherColumn(), Integer.valueOf(123))).isEqualTo("COUNT(*) BETWEEN `table`.`other` AND 123");
        assertCondition(() -> Function.count().isBetween(getOtherColumn(), 123)).isEqualTo("COUNT(*) BETWEEN `table`.`other` AND 123");
        assertCondition(() -> Function.count().isBetween(getOtherColumn(), 123.123)).isEqualTo("COUNT(*) BETWEEN `table`.`other` AND 123.123");
        assertCondition(() -> Function.count().isBetween(Integer.valueOf(123), getOtherColumn())).isEqualTo("COUNT(*) BETWEEN 123 AND `table`.`other`");
        assertCondition(() -> Function.count().isBetween(123, getOtherColumn())).isEqualTo("COUNT(*) BETWEEN 123 AND `table`.`other`");
        assertCondition(() -> Function.count().isBetween(123.123, getOtherColumn())).isEqualTo("COUNT(*) BETWEEN 123.123 AND `table`.`other`");
    }
}
