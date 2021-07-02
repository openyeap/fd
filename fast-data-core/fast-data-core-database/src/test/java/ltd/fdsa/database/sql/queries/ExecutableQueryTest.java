package ltd.fdsa.database.sql.queries;

import static ltd.fdsa.database.sql.queries.Queries.createTable;
import static org.powermock.api.easymock.PowerMock.createStrictMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import lombok.var;
import ltd.fdsa.database.sql.testsupport.QueryExecutorTestSupport;
import org.junit.jupiter.api.Test;

import ltd.fdsa.database.sql.schema.Table;

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
