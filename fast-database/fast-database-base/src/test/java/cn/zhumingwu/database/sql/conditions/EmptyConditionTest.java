package cn.zhumingwu.database.sql.conditions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmptyConditionTest
{
    @Test
    void testDoBuild()
    {
        assertThatThrownBy(() -> new EmptyCondition().doBuild(null, null)).isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("the EmptyCondition is not meant to be build");
    }
}
