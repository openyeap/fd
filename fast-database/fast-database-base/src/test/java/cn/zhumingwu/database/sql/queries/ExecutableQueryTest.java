package cn.zhumingwu.database.sql.queries;

import cn.zhumingwu.database.sql.testsupport.QueryExecutorTestSupport;

import cn.zhumingwu.database.sql.schema.Table;
import org.junit.jupiter.api.Test;

import static cn.zhumingwu.database.sql.queries.Queries.createTable;
import static org.powermock.api.easymock.PowerMock.*;

class ExecutableQueryTest extends QueryExecutorTestSupport
{
    private static final Table TABLE = Table.create("table");

    @Test
    void testQuery()
    {
        var runnable = createStrictMock(Runnable.class);
        var queryExecutor = new MyQueryExecutor(runnable);

        runnable.run();

        replayAll();
        createTable(TABLE).execute(queryExecutor);
        verifyAll();
    }
}
