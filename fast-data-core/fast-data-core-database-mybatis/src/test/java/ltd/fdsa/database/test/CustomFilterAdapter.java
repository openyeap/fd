package ltd.fdsa.database.test;

import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;

import java.sql.SQLException;

public class CustomFilterAdapter extends FilterAdapter {

    @Override
    public boolean preparedStatement_execute(FilterChain chain, PreparedStatementProxy statement) throws SQLException {


        return super.preparedStatement_execute(chain, statement);
    }

}
