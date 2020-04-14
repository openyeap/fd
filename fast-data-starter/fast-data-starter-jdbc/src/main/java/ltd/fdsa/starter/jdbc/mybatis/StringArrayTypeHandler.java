/**
 * @createTime: 2018年3月16日
 * @copyright: 上海道枢信息技术有限公司
 */
package ltd.fdsa.starter.jdbc.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * @classDesc: 功能描述: String数组类型和数据库VARCHAR类型的相互转换
 * @author Binrui Dong
 * @createTime 2018年3月16日 下午1:07:09
 * @version v1.0.0
 * @copyright: 上海道枢信息技术有限公司
 */
@MappedTypes({String[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringArrayTypeHandler implements TypeHandler<String[]> {

	@Override
	public void setParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
		if (parameter == null) {
			ps.setNull(i, Types.VARCHAR);
		} else {
			StringBuffer result = new StringBuffer();
			for (String value : parameter) {
				result.append(value).append(",");
			}
			result.deleteCharAt(result.length() - 1);
			ps.setString(i, result.toString());
		}
	}

	@Override
	public String[] getResult(ResultSet rs, String columnName) throws SQLException {
		return this.getStringArray(rs.getString(columnName));
	}

	@Override
	public String[] getResult(ResultSet rs, int columnIndex) throws SQLException {
		return this.getStringArray(rs.getString(columnIndex));
	}

	@Override
	public String[] getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return this.getStringArray(cs.getString(columnIndex));
	}

	private String[] getStringArray(String columnValue) {
		if (columnValue == null)
			return null;
		return columnValue.split(",");

	}
}
