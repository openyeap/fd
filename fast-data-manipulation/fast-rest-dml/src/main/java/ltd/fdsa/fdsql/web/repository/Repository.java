package ltd.fdsa.fdsql.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Map;
@Service
public class Repository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Object query(String sql) {
		return jdbcTemplate.query(sql,   new RowDataMapper()); 
	}
 	public Object create(String table, final Map<String, Object> row) {
		final String sql = "insert into users(name,email) values(?,?)";

		KeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//                ps.setArray(parameterIndex, x);(1, row.get(key));

				return ps;
			}
		}, holder);

		Number newId = holder.getKey();
		return newId;
	}

	public void delete(String table, final Integer id) {
		final String sql = "delete from users where id=?";
		jdbcTemplate.update(sql, new Object[] { id }, new int[] { java.sql.Types.INTEGER });
	}

	public void update(final Map<String, Object> row) {
		jdbcTemplate.update("update users set name=?,email=? where id=?", new Object[] { row.get("1"), row.get("2") });
	}
}
