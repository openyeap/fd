package cn.zhumingwu.database.sql.functions;

import cn.zhumingwu.database.sql.testsupport.OtherColumnTestSupport;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import static java.sql.Types.DOUBLE;
import static java.sql.Types.INTEGER;
import static cn.zhumingwu.database.sql.dialect.Dialects.MYSQL;
import static cn.zhumingwu.database.sql.queries.Queries.select;
import static org.assertj.core.api.Assertions.assertThat;

class AvgTest implements OtherColumnTestSupport
{
    private static final Table TABLE = Table.create("table");

    @Test
    void testCount()
    {
        assertThat(select(Function.avg(getOtherColumn()).as("alias")).from(TABLE).build(MYSQL)).isEqualTo("SELECT AVG(`table`.`other`) AS `alias` FROM `table`");
    }

    @Test
    void testGetSqlType()
    {
        assertThat(Function.avg(getOtherColumn()).getSqlType()).isEqualTo(DOUBLE);
        assertThat(Function.avg(getOtherColumn2()).getSqlType()).isEqualTo(INTEGER);
    }
}
