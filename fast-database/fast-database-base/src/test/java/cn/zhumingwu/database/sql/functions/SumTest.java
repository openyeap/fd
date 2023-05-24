package cn.zhumingwu.database.sql.functions;

import cn.zhumingwu.database.sql.testsupport.OtherColumnTestSupport;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import static java.sql.Types.DOUBLE;
import static java.sql.Types.INTEGER;
import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.queries.Queries.select;
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
        assertThat(Function.sum(getOtherColumn()).getSqlType()).isEqualTo(DOUBLE);
        assertThat(Function.sum(getOtherColumn2()).getSqlType()).isEqualTo(INTEGER);
    }
}
