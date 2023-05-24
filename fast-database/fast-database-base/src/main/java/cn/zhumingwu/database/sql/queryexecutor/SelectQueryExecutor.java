package cn.zhumingwu.database.sql.queryexecutor;

import cn.zhumingwu.database.sql.queries.Query;

import java.util.List;
import java.util.Optional;

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
