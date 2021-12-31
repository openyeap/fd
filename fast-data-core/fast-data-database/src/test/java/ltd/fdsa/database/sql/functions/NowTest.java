package ltd.fdsa.database.sql.functions;

import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.testsupport.OtherColumnTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static java.sql.Types.TIMESTAMP;
import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.queries.Queries.select;
import static org.assertj.core.api.Assertions.assertThat;

class NowTest implements OtherColumnTestSupport
{
    private static final Table TABLE = Table.create("table");

    @Test
    void testNow()
    {
        assertThat(select(Function.now().as("alias")).from(TABLE).build(MYSQL)).isEqualTo("SELECT NOW() AS `alias` FROM `table`");

        assertThat(select().from(TABLE)
                .where(Function.now().isBefore(Date.from(LocalDateTime.of(2020, 1, 5, 19, 41, 54).atZone(ZoneId.systemDefault()).toInstant())))
                .build(MYSQL)).isEqualTo("SELECT * FROM `table` WHERE NOW() < '2020-01-05 19:41:54.000000'");
    }

    @Test
    void testGetSqlType()
    {
        Assertions.assertThat(Function.now().getSqlType()).isEqualTo(TIMESTAMP);
    }
}
