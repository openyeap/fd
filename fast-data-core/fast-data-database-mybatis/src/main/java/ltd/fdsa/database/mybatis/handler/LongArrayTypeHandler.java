package ltd.fdsa.database.mybatis.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@MappedTypes({Long[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class LongArrayTypeHandler implements TypeHandler<Long[]> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.VARCHAR);
        } else {
            StringBuffer result = new StringBuffer();
            for (Long value : parameter) {
                result.append(value).append(",");
            }
            result.deleteCharAt(result.length() - 1);
            ps.setString(i, result.toString());
        }
    }

    @Override
    public Long[] getResult(ResultSet rs, String columnName) throws SQLException {
        return this.getLongArray(rs.getString(columnName));
    }

    @Override
    public Long[] getResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getLongArray(rs.getString(columnIndex));
    }

    @Override
    public Long[] getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getLongArray(cs.getString(columnIndex));
    }

    private Long[] getLongArray(String columnValue) {
        if (columnValue == null) {
            return null;
        }
        String[] strs = columnValue.split(",");
        List<Long> list = new ArrayList<Long>();
        if (strs.length > 0) {
            for (String string : strs) {
                list.add(Long.valueOf(string));
            }
        }
        return list.toArray(new Long[0]);
    }
}
