package ltd.fdsa.database.sql.functions;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.functions.Function.count;
import static ltd.fdsa.database.sql.queries.Queries.select;
import static java.sql.Types.INTEGER;
import static org.assertj.core.api.Assertions.assertThat;

import ltd.fdsa.database.sql.testsupport.OtherColumnTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.schema.Table;

class CountTest implements OtherColumnTestSupport
{
    private static final Table TABLE = Table.create("table");

    @Test
    void testCount()
    {
        assertThat(select(Function.count().as("alias")).from(TABLE).build(MYSQL)).isEqualTo("SELECT COUNT(*) AS `alias` FROM `table`");
        assertThat(select(Function.count(getOtherColumn()).as("alias")).from(TABLE).build(MYSQL)).isEqualTo("SELECT COUNT(`table`.`other`) AS `alias` FROM `table`");
    }

    @Test
    void testGetSqlType()
    {
        Assertions.assertThat(Function.count().getSqlType()).isEqualTo(INTEGER);
    }
}
