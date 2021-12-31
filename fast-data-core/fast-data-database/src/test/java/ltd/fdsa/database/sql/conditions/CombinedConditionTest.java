package ltd.fdsa.database.sql.conditions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CombinedConditionTest
{
    @Test
    void testGetCopy()
    {
        assertThat(CombinedCondition.getCopy(null)).isNull();
    }
}
