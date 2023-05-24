package cn.zhumingwu.database.sql.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DoubleSizeTest
{
    @Test
    void testValueOf()
    {
        assertThat(DoubleSize.valueOf(null)).isNull();
    }
}
