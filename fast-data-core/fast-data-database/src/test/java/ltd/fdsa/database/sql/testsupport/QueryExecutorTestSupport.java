package ltd.fdsa.database.sql.testsupport;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ltd.fdsa.database.sql.queries.ExecutableQuery;
import ltd.fdsa.database.sql.queries.Insert;
import ltd.fdsa.database.sql.queries.UpdatebleQuery;
import ltd.fdsa.database.sql.queryexecutor.QueryExecutor;
import ltd.fdsa.database.sql.queryexecutor.RowExtractor;
import ltd.fdsa.database.sql.queryexecutor.SelectQueryExecutor;

public class QueryExecutorTestSupport
{
    @AllArgsConstructor
    @NoArgsConstructor
    protected class MyQueryExecutor implements QueryExecutor
    {
        private Runnable runnable;

        @Override
        public <T> SelectQueryExecutor<T> select(RowExtractor<T> rowMapper)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> SelectQueryExecutor<T> select(Class<T> elementType)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public long execute(UpdatebleQuery updatebleQuery)
        {
            return 5;
        }

        @Override
        public long executeAndReturnKey(Insert insert)
        {
            return 3;
        }

        @Override
        public void execute(ExecutableQuery executableQuery)
        {
            runnable.run();
        }
    }
}
