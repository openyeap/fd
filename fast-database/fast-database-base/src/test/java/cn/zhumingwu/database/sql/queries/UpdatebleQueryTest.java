package cn.zhumingwu.database.sql.queries;

import cn.zhumingwu.database.sql.testsupport.QueryExecutorTestSupport;

import cn.zhumingwu.database.sql.columns.string.VarCharColumn;
import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import static cn.zhumingwu.database.sql.queries.Queries.insertInto;
import static cn.zhumingwu.database.sql.queries.Queries.update;
import static org.assertj.core.api.Assertions.assertThat;

class UpdatebleQueryTest extends QueryExecutorTestSupport
{
    private static final Table TABLE = Table.create("table");

    private static final VarCharColumn LASTNAME = TABLE.varCharColumn("lastname").build();

    @Test
    void testQuery()
    {

        var queryExecutor = new MyQueryExecutor();

        assertThat(update(TABLE).set(LASTNAME, "Schumacher").execute(queryExecutor)).isEqualTo(5);
    }

    @Test
    void testInsertQuery()
    {
        var queryExecutor = new MyQueryExecutor();

        assertThat(insertInto(TABLE).set(LASTNAME, "Schumacher").executeAndReturnKey(queryExecutor)).isEqualTo(3);
    }
}
