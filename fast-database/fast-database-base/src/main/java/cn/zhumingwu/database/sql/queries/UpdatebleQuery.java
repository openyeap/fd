package cn.zhumingwu.database.sql.queries;

import cn.zhumingwu.database.sql.queryexecutor.QueryExecutor;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface UpdatebleQuery extends Query
{
    default long execute(QueryExecutor queryExecutor)
    {
        return queryExecutor.execute(this);
    }
}
