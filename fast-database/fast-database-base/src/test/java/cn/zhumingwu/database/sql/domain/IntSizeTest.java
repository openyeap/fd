package cn.zhumingwu.database.sql.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntSizeTest
{
    @Test
    void testValueOf()
    {
        assertThat(IntSize.valueOf(null)).isNull();
    }
}
