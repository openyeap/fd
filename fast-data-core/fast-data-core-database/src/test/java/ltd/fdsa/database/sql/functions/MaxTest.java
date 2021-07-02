package ltd.fdsa.database.sql.functions;

import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.queries.Queries.select;
import static java.sql.Types.DOUBLE;
import static java.sql.Types.INTEGER;
import static org.assertj.core.api.Assertions.assertThat;

import ltd.fdsa.database.sql.testsupport.OtherColumnTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.schema.Table;

class MaxTest implements OtherColumnTestSupport
{
    private static final Table TABLE = Table.create("table");

    @Test
    void testMax()
    {
        assertThat(select(Function.max(getOtherColumn()).as("alias")).from(TABLE).build(MYSQL)).isEqualTo("SELECT MAX(`table`.`other`) AS `alias` FROM `table`");
    }

    @Test
    void testGetSqlType()
    {
        Assertions.assertThat(Function.max(getOtherColumn()).getSqlType()).isEqualTo(DOUBLE);
        Assertions.assertThat(Function.max(getOtherColumn2()).getSqlType()).isEqualTo(INTEGER);
    }
}
