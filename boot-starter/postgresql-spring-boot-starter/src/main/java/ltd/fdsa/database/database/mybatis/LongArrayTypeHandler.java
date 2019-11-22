/**
 * @createTime: 2018年3月16日
 * @copyright: 上海道枢信息技术有限公司
 */
package com.daoshu.datasource.database.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * @classDesc: 功能描述: Long数组转换为数据库的字符串
 * @author Binrui Dong
 * @createTime 2018年3月16日 下午4:28:36
 * @version v1.0.0
 * @copyright: 上海道枢信息技术有限公司
 */
@MappedTypes({ Long[].class })
@MappedJdbcTypes({ JdbcType.VARCHAR })
public class LongArrayTypeHandler implements TypeHandler<Long[]> {

	@Override
	public void setParameter(PreparedStatement ps, int i, Long[] parameter, JdbcType jdbcType) throws SQLException {
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
		if (columnValue == null)
			return null;
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
