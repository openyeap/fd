package ltd.fdsa.database.sql.queryexecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhumingwu
 *
 * @since 3/20/2021 10:36 AM
 */
public interface RowExtractor<T>
{
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
