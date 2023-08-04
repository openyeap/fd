package cn.zhumingwu.client.repository;

import cn.zhumingwu.client.entity.Client;
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
public class ClientRepository {

    static Cache<String, Expression> local = CacheBuilder.newBuilder().maximumSize(1024)
        .expireAfterAccess(10L, TimeUnit.MINUTES).removalListener(removalNotification -> { })
        .build();


    @Resource
    JdbcTemplate jdbcTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Client> page(int index, int pageSize, String order, String where, Object... param) {
        String limit = MessageFormat.format("LIMIT {0} OFFSET {1}", pageSize, pageSize * index);
        String sql = "SELECT cid,\nuid,\nname,\nphone_number,\nemail_address,\ntype,\nclient_code,\nclient_secret,\nallow_scopes,\ngrant_type,\ntoken_lifetime,\naccess_token_lifetime,\nauthorization_code_lifetime,\nrefresh_token_lifetime,\nclaim_prefix,\nissue_ts,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_client " +
             where +
             order +
             limit;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }


    public List<Client> query(String where, Object... param) {
        String sql = "SELECT cid,\nuid,\nname,\nphone_number,\nemail_address,\ntype,\nclient_code,\nclient_secret,\nallow_scopes,\ngrant_type,\ntoken_lifetime,\naccess_token_lifetime,\nauthorization_code_lifetime,\nrefresh_token_lifetime,\nclaim_prefix,\nissue_ts,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_client " + where;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }

    public List<Client> queryByPrimaryKey(Object key) {
        String sql = "SELECT cid,\nuid,\nname,\nphone_number,\nemail_address,\ntype,\nclient_code,\nclient_secret,\nallow_scopes,\ngrant_type,\ntoken_lifetime,\naccess_token_lifetime,\nauthorization_code_lifetime,\nrefresh_token_lifetime,\nclaim_prefix,\nissue_ts,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_client  WHERE cid=? \n;";
        return this.jdbcTemplate.query(sql, this::extractData, key);
    }

    public int insert(Client... entities) {
        String sql = "INSERT INTO t_client (cid,\nuid,\nname,\nphone_number,\nemail_address,\ntype,\nclient_code,\nclient_secret,\nallow_scopes,\ngrant_type,\ntoken_lifetime,\naccess_token_lifetime,\nauthorization_code_lifetime,\nrefresh_token_lifetime,\nclaim_prefix,\nissue_ts,\nexpires_in,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\n) VALUES (\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?\n)";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var userId = entity.getUserId();
            var name = entity.getName();
            var phoneNumber = entity.getPhoneNumber();
            var emailAddress = entity.getEmailAddress();
            var type = entity.getType();
            var clientCode = entity.getClientCode();
            var clientSecret = entity.getClientSecret();
            var allowScopes = entity.getAllowScopes();
            var grant = entity.getGrant();
            var tokenLifetime = entity.getTokenLifetime();
            var accessTokenLifetime = entity.getAccessTokenLifetime();
            var authorizationCodeLifetime = entity.getAuthorizationCodeLifetime();
            var refreshTokenLifetime = entity.getRefreshTokenLifetime();
            var claimPrefix = entity.getClaimPrefix();
            var issueTime = entity.getIssueTime();
            var expiresIn = entity.getExpiresIn();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, id,userId,name,phoneNumber,emailAddress,type,clientCode,clientSecret,allowScopes,grant,tokenLifetime,accessTokenLifetime,authorizationCodeLifetime,refreshTokenLifetime,claimPrefix,issueTime,expiresIn,createdTime,updatedTime,createdBy,updatedBy,status);
        }
        return result;
    }

    public int update(Client... entities) {

        String sql = "UPDATE t_client SET uid=?,\nname=?,\nphone_number=?,\nemail_address=?,\ntype=?,\nclient_code=?,\nclient_secret=?,\nallow_scopes=?,\ngrant_type=?,\ntoken_lifetime=?,\naccess_token_lifetime=?,\nauthorization_code_lifetime=?,\nrefresh_token_lifetime=?,\nclaim_prefix=?,\nissue_ts=?,\nexpires_in=?,\ncreate_time=?,\nupdate_time=?,\ncreate_by=?,\nupdate_by=?,\nstatus=?\n WHERE cid=? \n;";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var userId = entity.getUserId();
            var name = entity.getName();
            var phoneNumber = entity.getPhoneNumber();
            var emailAddress = entity.getEmailAddress();
            var type = entity.getType();
            var clientCode = entity.getClientCode();
            var clientSecret = entity.getClientSecret();
            var allowScopes = entity.getAllowScopes();
            var grant = entity.getGrant();
            var tokenLifetime = entity.getTokenLifetime();
            var accessTokenLifetime = entity.getAccessTokenLifetime();
            var authorizationCodeLifetime = entity.getAuthorizationCodeLifetime();
            var refreshTokenLifetime = entity.getRefreshTokenLifetime();
            var claimPrefix = entity.getClaimPrefix();
            var issueTime = entity.getIssueTime();
            var expiresIn = entity.getExpiresIn();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, userId,name,phoneNumber,emailAddress,type,clientCode,clientSecret,allowScopes,grant,tokenLifetime,accessTokenLifetime,authorizationCodeLifetime,refreshTokenLifetime,claimPrefix,issueTime,expiresIn,createdTime,updatedTime,createdBy,updatedBy,status,id);
        }
        return result;
    }

    public int delete(int... ids) {
        String sql = "UPDATE t_client SET status = -1 WHERE cid=? \n;";
        int result = 0;
        for (var id : ids) {
            result += this.jdbcTemplate.update(sql, id);
        }
        return result;
    }

    public int deleteAll(Integer... ids) {
        String sql = "DELETE FROM t_client WHERE cid=? \n;";
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

    List<Client> extractData(ResultSet rs)  {
        List<Client> list = new ArrayList<>();
		try {
            while (rs.next()) {
                Client entity = new Client();
                entity.setId(rs.getLong("cid"));
                entity.setUserId(rs.getLong("uid"));
                entity.setName(rs.getString("name"));
                entity.setPhoneNumber(rs.getString("phone_number"));
                entity.setEmailAddress(rs.getString("email_address"));
                // todo: ClientType ;
                entity.setClientCode(rs.getString("client_code"));
                entity.setClientSecret(rs.getString("client_secret"));
                entity.setAllowScopes(rs.getString("allow_scopes"));
                // todo: GrantType ;
                entity.setTokenLifetime(rs.getLong("token_lifetime"));
                entity.setAccessTokenLifetime(rs.getLong("access_token_lifetime"));
                entity.setAuthorizationCodeLifetime(rs.getLong("authorization_code_lifetime"));
                entity.setRefreshTokenLifetime(rs.getLong("refresh_token_lifetime"));
                entity.setClaimPrefix(rs.getString("claim_prefix"));
                entity.setIssueTime(rs.getDate("issue_ts"));
                entity.setExpiresIn(rs.getLong("expires_in"));
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
