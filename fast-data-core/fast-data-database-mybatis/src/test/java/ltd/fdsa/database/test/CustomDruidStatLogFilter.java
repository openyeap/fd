package ltd.fdsa.database.test;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomDruidStatLogFilter extends FilterEventAdapter {
    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        internalAfterStatementExecute(statement);
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        internalAfterStatementExecute(statement);
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean firstResult) {
        internalAfterStatementExecute(statement);
    }

    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        internalAfterStatementExecute(statement);
    }

    /**
     * 核心记录方法：判断记录慢sql
     */
    private void internalAfterStatementExecute(StatementProxy statement) {
        if (statement.getSqlStat() != null) {
            String sql = statement.getLastExecuteSql();
        }
    }
}
