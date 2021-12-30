package ltd.fdsa.database.sql.queries;

import ltd.fdsa.database.sql.queryexecutor.QueryExecutor;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface ExecutableQuery extends Query
{
    default void execute(QueryExecutor queryExecutor)
    {
        queryExecutor.execute(this);
    }
}
