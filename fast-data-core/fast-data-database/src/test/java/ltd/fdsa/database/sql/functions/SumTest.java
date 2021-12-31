package ltd.fdsa.database.sql.functions;

import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.testsupport.OtherColumnTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.sql.Types.DOUBLE;
import static java.sql.Types.INTEGER;
import static ltd.fdsa.database.sql.dialect.Dialects.MYSQL;
import static ltd.fdsa.database.sql.queries.Queries.select;
import static org.assertj.core.api.Assertions.assertThat;

class SumTest implements OtherColumnTestSupport
{
    private static final Table TABLE = Table.create("table");

    @Test
    void testSum()
    {
        assertThat(select(Function.sum(getOtherColumn()).as("alias")).from(TABLE).build(MYSQL)).isEqualTo("SELECT SUM(`table`.`other`) AS `alias` FROM `table`");
    }

    @Test
    void testGetSqlType()
    {
        Assertions.assertThat(Function.sum(getOtherColumn()).getSqlType()).isEqualTo(DOUBLE);
        Assertions.assertThat(Function.sum(getOtherColumn2()).getSqlType()).isEqualTo(INTEGER);
    }
}
