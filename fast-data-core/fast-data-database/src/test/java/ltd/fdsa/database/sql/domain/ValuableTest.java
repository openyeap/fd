package ltd.fdsa.database.sql.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValuableTest
{
    @Test
    void testPlain()
    {
        assertThat(((Plain) Valuable.plain("anyValue").getValue()).getValue()).isEqualTo("anyValue");
    }
}
