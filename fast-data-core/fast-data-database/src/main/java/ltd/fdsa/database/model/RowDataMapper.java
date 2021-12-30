package ltd.fdsa.database.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RowDataMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData(); // 获取键名
        int columnCount = metaData.getColumnCount(); // 获取行的数量
        List<String> list = new ArrayList<String>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            list.add(metaData.getColumnName(i));
        }

        Map<String, Object> rowData = new HashMap<String, Object>();
        for (int i = 1; i <= columnCount; i++) {
            rowData.put(metaData.getColumnName(i), rs.getObject(i)); // 获取键名及值
        }
        return rowData;
    }
}
