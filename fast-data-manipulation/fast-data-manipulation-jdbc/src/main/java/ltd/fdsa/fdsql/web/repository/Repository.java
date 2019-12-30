package ltd.fdsa.fdsql.web.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Service
public class Repository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Object query(String sql) {
        return jdbcTemplate.query(sql, new RowDataMapper());
    }

    public Object create(String sql, String[] data) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement ps = jdbcTemplate.getDataSource()
                        .getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int j = 0; j < data.length; j++) {
                    ps.setString(j + 1, data[j]);
                }
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKeyList().get(0);
    }

    public int update(String sql, List<Object> list) {
        if (list == null || list.size() == 0) {
            return jdbcTemplate.update(sql);
        }
        return jdbcTemplate.update(sql, list.toArray());
    }
}
