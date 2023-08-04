package cn.zhumingwu.client.repository;

import cn.zhumingwu.client.entity.UserConnect;
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
public class UserConnectRepository {

    static Cache<String, Expression> local = CacheBuilder.newBuilder().maximumSize(1024)
        .expireAfterAccess(10L, TimeUnit.MINUTES).removalListener(removalNotification -> { })
        .build();


    @Resource
    JdbcTemplate jdbcTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<UserConnect> page(int index, int pageSize, String order, String where, Object... param) {
        String limit = MessageFormat.format("LIMIT {0} OFFSET {1}", pageSize, pageSize * index);
        String sql = "SELECT user_connect_id,\nuid,\nconnect_provider_id,\naccess_token,\nrefresh_token,\nexpires_in,\nrefresh_expires_in,\nissue_ts,\nopen_id,\nunion_id,\nuser_nick_name,\nextensions,\nscopes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user_connect " +
             where +
             order +
             limit;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }


    public List<UserConnect> query(String where, Object... param) {
        String sql = "SELECT user_connect_id,\nuid,\nconnect_provider_id,\naccess_token,\nrefresh_token,\nexpires_in,\nrefresh_expires_in,\nissue_ts,\nopen_id,\nunion_id,\nuser_nick_name,\nextensions,\nscopes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user_connect " + where;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }

    public List<UserConnect> queryByPrimaryKey(Object key) {
        String sql = "SELECT user_connect_id,\nuid,\nconnect_provider_id,\naccess_token,\nrefresh_token,\nexpires_in,\nrefresh_expires_in,\nissue_ts,\nopen_id,\nunion_id,\nuser_nick_name,\nextensions,\nscopes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user_connect  WHERE user_connect_id=? \n;";
        return this.jdbcTemplate.query(sql, this::extractData, key);
    }

    public int insert(UserConnect... entities) {
        String sql = "INSERT INTO t_user_connect (user_connect_id,\nuid,\nconnect_provider_id,\naccess_token,\nrefresh_token,\nexpires_in,\nrefresh_expires_in,\nissue_ts,\nopen_id,\nunion_id,\nuser_nick_name,\nextensions,\nscopes,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\n) VALUES (\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?\n)";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var userId = entity.getUserId();
            var connectProviderId = entity.getConnectProviderId();
            var accessToken = entity.getAccessToken();
            var refreshToken = entity.getRefreshToken();
            var expiresIn = entity.getExpiresIn();
            var refreshExpiresIn = entity.getRefreshExpiresIn();
            var issueTime = entity.getIssueTime();
            var openId = entity.getOpenId();
            var unionId = entity.getUnionId();
            var userNickName = entity.getUserNickName();
            var extensions = entity.getExtensions();
            var scopes = entity.getScopes();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, id,userId,connectProviderId,accessToken,refreshToken,expiresIn,refreshExpiresIn,issueTime,openId,unionId,userNickName,extensions,scopes,createdTime,updatedTime,createdBy,updatedBy,status);
        }
        return result;
    }

    public int update(UserConnect... entities) {

        String sql = "UPDATE t_user_connect SET uid=?,\nconnect_provider_id=?,\naccess_token=?,\nrefresh_token=?,\nexpires_in=?,\nrefresh_expires_in=?,\nissue_ts=?,\nopen_id=?,\nunion_id=?,\nuser_nick_name=?,\nextensions=?,\nscopes=?,\ncreate_time=?,\nupdate_time=?,\ncreate_by=?,\nupdate_by=?,\nstatus=?\n WHERE user_connect_id=? \n;";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var userId = entity.getUserId();
            var connectProviderId = entity.getConnectProviderId();
            var accessToken = entity.getAccessToken();
            var refreshToken = entity.getRefreshToken();
            var expiresIn = entity.getExpiresIn();
            var refreshExpiresIn = entity.getRefreshExpiresIn();
            var issueTime = entity.getIssueTime();
            var openId = entity.getOpenId();
            var unionId = entity.getUnionId();
            var userNickName = entity.getUserNickName();
            var extensions = entity.getExtensions();
            var scopes = entity.getScopes();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, userId,connectProviderId,accessToken,refreshToken,expiresIn,refreshExpiresIn,issueTime,openId,unionId,userNickName,extensions,scopes,createdTime,updatedTime,createdBy,updatedBy,status,id);
        }
        return result;
    }

    public int delete(int... ids) {
        String sql = "UPDATE t_user_connect SET status = -1 WHERE user_connect_id=? \n;";
        int result = 0;
        for (var id : ids) {
            result += this.jdbcTemplate.update(sql, id);
        }
        return result;
    }

    public int deleteAll(Integer... ids) {
        String sql = "DELETE FROM t_user_connect WHERE user_connect_id=? \n;";
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

    List<UserConnect> extractData(ResultSet rs)  {
        List<UserConnect> list = new ArrayList<>();
		try {
            while (rs.next()) {
                UserConnect entity = new UserConnect();
                entity.setId(rs.getLong("user_connect_id"));
                entity.setUserId(rs.getLong("uid"));
                entity.setConnectProviderId(rs.getLong("connect_provider_id"));
                entity.setAccessToken(rs.getString("access_token"));
                entity.setRefreshToken(rs.getString("refresh_token"));
                entity.setExpiresIn(rs.getLong("expires_in"));
                entity.setRefreshExpiresIn(rs.getLong("refresh_expires_in"));
                entity.setIssueTime(rs.getDate("issue_ts"));
                entity.setOpenId(rs.getString("open_id"));
                entity.setUnionId(rs.getString("union_id"));
                entity.setUserNickName(rs.getString("user_nick_name"));
                entity.setExtensions(rs.getString("extensions"));
                entity.setScopes(rs.getString("scopes"));
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
