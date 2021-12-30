package ltd.fdsa.database.sql.queryexecutor;

import java.util.List;
import java.util.Optional;

import ltd.fdsa.database.sql.queries.Query;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface SelectQueryExecutor<T>
{
    List<T> query(Query select);

    Optional<T> queryOne(Query select);

    T queryExactOne(Query query);
}
