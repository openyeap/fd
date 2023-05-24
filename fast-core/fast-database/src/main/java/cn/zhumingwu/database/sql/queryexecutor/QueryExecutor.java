package cn.zhumingwu.database.sql.queryexecutor;

import cn.zhumingwu.database.sql.queries.ExecutableQuery;
import cn.zhumingwu.database.sql.queries.Insert;
import cn.zhumingwu.database.sql.queries.UpdatebleQuery;

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
