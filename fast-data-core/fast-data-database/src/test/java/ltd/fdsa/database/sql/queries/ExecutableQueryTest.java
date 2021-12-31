package ltd.fdsa.database.sql.queries;

import lombok.var;
import ltd.fdsa.database.sql.schema.Table;
import ltd.fdsa.database.sql.testsupport.QueryExecutorTestSupport;
import org.junit.jupiter.api.Test;

import static ltd.fdsa.database.sql.queries.Queries.createTable;
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
