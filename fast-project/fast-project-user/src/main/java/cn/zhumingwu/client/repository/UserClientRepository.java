package cn.zhumingwu.client.repository;

import cn.zhumingwu.client.entity.UserClient;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.annotation.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserClientRepository {

    static Cache<String, Expression> local = CacheBuilder.newBuilder().maximumSize(1024)
        .expireAfterAccess(10L, TimeUnit.MINUTES).removalListener(removalNotification -> { })
        .build();


    @Resource
    JdbcTemplate jdbcTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<UserClient> page(int index, int pageSize, String order, String where, Object... param) {
        String limit = MessageFormat.format("LIMIT {0} OFFSET {1}", pageSize, pageSize * index);
        String sql = "SELECT user_client_id,\ncid,\nuid\nFROM t_user_client " +
             where +
             order +
             limit;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }


    public List<UserClient> query(String where, Object... param) {
        String sql = "SELECT user_client_id,\ncid,\nuid\nFROM t_user_client " + where;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }

    public List<UserClient> queryByPrimaryKey(Object key) {
        String sql = "SELECT user_client_id,\ncid,\nuid\nFROM t_user_client  WHERE user_client_id=? \n;";
        return this.jdbcTemplate.query(sql, this::extractData, key);
    }

    public int insert(UserClient... entities) {
        String sql = "INSERT INTO t_user_client (user_client_id,\ncid,\nuid\n) VALUES (\n?,\n?,\n?\n)";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var clientId = entity.getClientId();
            var userId = entity.getUserId();
            result += this.jdbcTemplate.update( sql, id,clientId,userId);
        }
        return result;
    }

    public int update(UserClient... entities) {

        String sql = "UPDATE t_user_client SET cid=?,\nuid=?\n WHERE user_client_id=? \n;";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var clientId = entity.getClientId();
            var userId = entity.getUserId();
            result += this.jdbcTemplate.update( sql, clientId,userId,id);
        }
        return result;
    }

    public int delete(int... ids) {
        String sql = "UPDATE t_user_client SET status = -1 WHERE user_client_id=? \n;";
        int result = 0;
        for (var id : ids) {
            result += this.jdbcTemplate.update(sql, id);
        }
        return result;
    }

    public int deleteAll(Integer... ids) {
        String sql = "DELETE FROM t_user_client WHERE user_client_id=? \n;";
        var list = Arrays.asList(ids);
        int result = 0;
        for (var partition : Lists.partition(list, 1000)) {
            result += Arrays.stream(this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, partition.get(i));
                }
                @Override
                public int getBatchSize() {
                    return partition.size();
                }
            })).sum();
        }
        return result;
    }

    public <T> T getNamedQuery(String sql, ResultSetExtractor<T> rse, Map<String, ?> paramMap) {
        return this.namedParameterJdbcTemplate.query(sql, paramMap, rse);
    }

    public <T> T getQuery(String sql, ResultSetExtractor<T> rse, @Nullable Object... args) {
        return this.jdbcTemplate.query(sql, rse, args);
    }

    public <T> T getExpQuery(String spel, ResultSetExtractor<T> rse, Map<String, ?> paramMap) {
        var expression = local.getIfPresent(spel);
        if (expression == null) {
            expression = this.parser.parseExpression(spel);
            local.put(spel, expression);
        }
        var sql = expression.getValue(paramMap, String.class);
        assert sql != null;
        return this.jdbcTemplate.query(sql, rse);
    }

    List<UserClient> extractData(ResultSet rs)  {
        List<UserClient> list = new ArrayList<>();
		try {
            while (rs.next()) {
                UserClient entity = new UserClient();
                entity.setId(rs.getLong("user_client_id"));
                entity.setClientId(rs.getLong("cid"));
                entity.setUserId(rs.getLong("uid"));
                list.add(entity);
            }
        } catch (SQLException e) {
            log.error("error", e);
        }
        return list;
    }
}
