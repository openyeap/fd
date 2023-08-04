package cn.zhumingwu.client.repository;

import cn.zhumingwu.client.entity.ConnectProvider;
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
public class ConnectProviderRepository {

    static Cache<String, Expression> local = CacheBuilder.newBuilder().maximumSize(1024)
        .expireAfterAccess(10L, TimeUnit.MINUTES).removalListener(removalNotification -> { })
        .build();


    @Resource
    JdbcTemplate jdbcTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<ConnectProvider> page(int index, int pageSize, String order, String where, Object... param) {
        String limit = MessageFormat.format("LIMIT {0} OFFSET {1}", pageSize, pageSize * index);
        String sql = "SELECT connect_id,\nname,\ndescription,\nlogo_uri,\napp_key,\napp_secret,\nscope,\nrequest_uri,\nredirect_uri,\nresponse_type,\naccess_token_uri,\ngrant_type,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_connect_provider " +
             where +
             order +
             limit;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }


    public List<ConnectProvider> query(String where, Object... param) {
        String sql = "SELECT connect_id,\nname,\ndescription,\nlogo_uri,\napp_key,\napp_secret,\nscope,\nrequest_uri,\nredirect_uri,\nresponse_type,\naccess_token_uri,\ngrant_type,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_connect_provider " + where;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }

    public List<ConnectProvider> queryByPrimaryKey(Object key) {
        String sql = "SELECT connect_id,\nname,\ndescription,\nlogo_uri,\napp_key,\napp_secret,\nscope,\nrequest_uri,\nredirect_uri,\nresponse_type,\naccess_token_uri,\ngrant_type,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_connect_provider  WHERE connect_id=? \n;";
        return this.jdbcTemplate.query(sql, this::extractData, key);
    }

    public int insert(ConnectProvider... entities) {
        String sql = "INSERT INTO t_connect_provider (connect_id,\nname,\ndescription,\nlogo_uri,\napp_key,\napp_secret,\nscope,\nrequest_uri,\nredirect_uri,\nresponse_type,\naccess_token_uri,\ngrant_type,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\n) VALUES (\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?\n)";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var name = entity.getName();
            var description = entity.getDescription();
            var logoUri = entity.getLogoUri();
            var clientCode = entity.getClientCode();
            var clientSecret = entity.getClientSecret();
            var scope = entity.getScope();
            var requestUri = entity.getRequestUri();
            var redirectUri = entity.getRedirectUri();
            var responseType = entity.getResponseType();
            var accessTokenUri = entity.getAccessTokenUri();
            var grantType = entity.getGrantType();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, id,name,description,logoUri,clientCode,clientSecret,scope,requestUri,redirectUri,responseType,accessTokenUri,grantType,createdTime,updatedTime,createdBy,updatedBy,status);
        }
        return result;
    }

    public int update(ConnectProvider... entities) {

        String sql = "UPDATE t_connect_provider SET name=?,\ndescription=?,\nlogo_uri=?,\napp_key=?,\napp_secret=?,\nscope=?,\nrequest_uri=?,\nredirect_uri=?,\nresponse_type=?,\naccess_token_uri=?,\ngrant_type=?,\ncreate_time=?,\nupdate_time=?,\ncreate_by=?,\nupdate_by=?,\nstatus=?\n WHERE connect_id=? \n;";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var name = entity.getName();
            var description = entity.getDescription();
            var logoUri = entity.getLogoUri();
            var clientCode = entity.getClientCode();
            var clientSecret = entity.getClientSecret();
            var scope = entity.getScope();
            var requestUri = entity.getRequestUri();
            var redirectUri = entity.getRedirectUri();
            var responseType = entity.getResponseType();
            var accessTokenUri = entity.getAccessTokenUri();
            var grantType = entity.getGrantType();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, name,description,logoUri,clientCode,clientSecret,scope,requestUri,redirectUri,responseType,accessTokenUri,grantType,createdTime,updatedTime,createdBy,updatedBy,status,id);
        }
        return result;
    }

    public int delete(int... ids) {
        String sql = "UPDATE t_connect_provider SET status = -1 WHERE connect_id=? \n;";
        int result = 0;
        for (var id : ids) {
            result += this.jdbcTemplate.update(sql, id);
        }
        return result;
    }

    public int deleteAll(Integer... ids) {
        String sql = "DELETE FROM t_connect_provider WHERE connect_id=? \n;";
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

    List<ConnectProvider> extractData(ResultSet rs)  {
        List<ConnectProvider> list = new ArrayList<>();
		try {
            while (rs.next()) {
                ConnectProvider entity = new ConnectProvider();
                entity.setId(rs.getLong("connect_id"));
                entity.setName(rs.getString("name"));
                entity.setDescription(rs.getString("description"));
                entity.setLogoUri(rs.getString("logo_uri"));
                entity.setClientCode(rs.getString("app_key"));
                entity.setClientSecret(rs.getString("app_secret"));
                entity.setScope(rs.getString("scope"));
                entity.setRequestUri(rs.getString("request_uri"));
                entity.setRedirectUri(rs.getString("redirect_uri"));
                entity.setResponseType(rs.getString("response_type"));
                entity.setAccessTokenUri(rs.getString("access_token_uri"));
                entity.setGrantType(rs.getString("grant_type"));
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
