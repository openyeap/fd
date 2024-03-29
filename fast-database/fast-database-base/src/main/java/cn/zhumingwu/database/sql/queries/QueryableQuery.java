package cn.zhumingwu.database.sql.queries;

import cn.zhumingwu.database.sql.queryexecutor.SelectQueryExecutor;

import java.util.List;
import java.util.Optional;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface QueryableQuery extends Query
{
    default <T> List<T> query(SelectQueryExecutor<T> selectQueryExecutor)
    {
        return selectQueryExecutor.query(this);
    }

    default <T> Optional<T> queryOne(SelectQueryExecutor<T> selectQueryExecutor)
    {
        return selectQueryExecutor.queryOne(this);
    }

    default <T> T queryExactOne(SelectQueryExecutor<T> selectQueryExecutor)
    {
        return selectQueryExecutor.queryExactOne(this);
    }
}
