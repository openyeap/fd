package ltd.fdsa.database.sql.queryexecutor;

import ltd.fdsa.database.sql.queries.UpdatebleQuery;
import ltd.fdsa.database.sql.queries.ExecutableQuery;
import ltd.fdsa.database.sql.queries.Insert;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface QueryExecutor
{
    <T> SelectQueryExecutor<T> select(RowExtractor<T> rowMapper);

    <T> SelectQueryExecutor<T> select(Class<T> elementType);

    long execute(UpdatebleQuery updatebleQuery);

    long executeAndReturnKey(Insert insert);

    void execute(ExecutableQuery executableQuery);
}
