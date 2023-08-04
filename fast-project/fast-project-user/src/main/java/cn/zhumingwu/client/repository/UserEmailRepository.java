package cn.zhumingwu.client.repository;

import cn.zhumingwu.client.entity.UserEmail;
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
public class UserEmailRepository {

    static Cache<String, Expression> local = CacheBuilder.newBuilder().maximumSize(1024)
        .expireAfterAccess(10L, TimeUnit.MINUTES).removalListener(removalNotification -> { })
        .build();


    @Resource
    JdbcTemplate jdbcTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<UserEmail> page(int index, int pageSize, String order, String where, Object... param) {
        String limit = MessageFormat.format("LIMIT {0} OFFSET {1}", pageSize, pageSize * index);
        String sql = "SELECT user_email_id,\nuid,\nemail_address,\nvalidate_code,\nvalidate_time,\nconfirm_time,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user_email " +
             where +
             order +
             limit;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }


    public List<UserEmail> query(String where, Object... param) {
        String sql = "SELECT user_email_id,\nuid,\nemail_address,\nvalidate_code,\nvalidate_time,\nconfirm_time,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user_email " + where;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }

    public List<UserEmail> queryByPrimaryKey(Object key) {
        String sql = "SELECT user_email_id,\nuid,\nemail_address,\nvalidate_code,\nvalidate_time,\nconfirm_time,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user_email  WHERE user_email_id=? and \nuid=? \n;";
        return this.jdbcTemplate.query(sql, this::extractData, key);
    }

    public int insert(UserEmail... entities) {
        String sql = "INSERT INTO t_user_email (user_email_id,\nuid,\nemail_address,\nvalidate_code,\nvalidate_time,\nconfirm_time,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\n) VALUES (\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?\n)";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var emailAddress = entity.getEmailAddress();
            var validateCode = entity.getValidateCode();
            var validateTime = entity.getValidateTime();
            var confirmedTime = entity.getConfirmedTime();
            var expiresIn = entity.getExpiresIn();
            var userId = entity.getUserId();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, id,userId,emailAddress,validateCode,validateTime,confirmedTime,expiresIn,createdTime,updatedTime,createdBy,updatedBy,status);
        }
        return result;
    }

    public int update(UserEmail... entities) {

        String sql = "UPDATE t_user_email SET email_address=?,\nvalidate_code=?,\nvalidate_time=?,\nconfirm_time=?,\nexpires_in=?,\ncreate_time=?,\nupdate_time=?,\ncreate_by=?,\nupdate_by=?,\nstatus=?\n WHERE user_email_id=? and \nuid=? \n;";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var emailAddress = entity.getEmailAddress();
            var validateCode = entity.getValidateCode();
            var validateTime = entity.getValidateTime();
            var confirmedTime = entity.getConfirmedTime();
            var expiresIn = entity.getExpiresIn();
            var userId = entity.getUserId();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, emailAddress,validateCode,validateTime,confirmedTime,expiresIn,createdTime,updatedTime,createdBy,updatedBy,status,id,userId);
        }
        return result;
    }

    public int delete(int... ids) {
        String sql = "UPDATE t_user_email SET status = -1 WHERE user_email_id=? and \nuid=? \n;";
        int result = 0;
        for (var id : ids) {
            result += this.jdbcTemplate.update(sql, id);
        }
        return result;
    }

    public int deleteAll(Integer... ids) {
        String sql = "DELETE FROM t_user_email WHERE user_email_id=? and \nuid=? \n;";
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

    List<UserEmail> extractData(ResultSet rs)  {
        List<UserEmail> list = new ArrayList<>();
		try {
            while (rs.next()) {
                UserEmail entity = new UserEmail();
                entity.setId(rs.getLong("user_email_id"));
                entity.setEmailAddress(rs.getString("email_address"));
                entity.setValidateCode(rs.getString("validate_code"));
                entity.setValidateTime(rs.getDate("validate_time"));
                entity.setConfirmedTime(rs.getDate("confirm_time"));
                entity.setExpiresIn(rs.getLong("expires_in"));
                entity.setUserId(rs.getLong("uid"));
                entity.setCreatedTime(rs.getDate("create_time"));
                entity.setUpdatedTime(rs.getDate("update_time"));
                entity.setCreatedBy(rs.getLong("create_by"));
                entity.setUpdatedBy(rs.getLong("update_by"));
                // todo: Status ;
                list.add(entity);
            }
        } catch (SQLException e) {
            log.error("error", e);
        }
        return list;
    }
}
