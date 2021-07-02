package ltd.fdsa.database.sql.conditions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CombinedConditionTest
{
    @Test
    void testGetCopy()
    {
        assertThat(CombinedCondition.getCopy(null)).isNull();
    }
}
