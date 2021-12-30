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

class MinTest implements OtherColumnTestSupport
{
    private static final Table TABLE = Table.create("table");

    @Test
    void testMin()
    {
        assertThat(select(Function.min(getOtherColumn()).as("alias")).from(TABLE).build(MYSQL)).isEqualTo("SELECT MIN(`table`.`other`) AS `alias` FROM `table`");
    }

    @Test
    void testGetSqlType()
    {
        Assertions.assertThat(Function.min(getOtherColumn()).getSqlType()).isEqualTo(DOUBLE);
        Assertions.assertThat(Function.min(getOtherColumn2()).getSqlType()).isEqualTo(INTEGER);
    }
}
