package cn.zhumingwu.database.sql.domain;

import lombok.var;
import org.junit.jupiter.api.Test;

import static java.lang.System.lineSeparator;
import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.utils.Indentation.enabled;
import static org.assertj.core.api.Assertions.assertThat;

class BuildingContextTest
{
    @Test
    void testAll()
    {
        var context = new BuildingContext(MYSQL, enabled().getDelimiter());
        System.out.println(context);
        assertThat(context.getDialect()).isSameAs(MYSQL);
        assertThat(context.getDelimiter()).isEqualTo(lineSeparator());
    }
}
