package cn.zhumingwu.client.repository;

import cn.zhumingwu.client.entity.User;
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
public class UserRepository {

    static Cache<String, Expression> local = CacheBuilder.newBuilder().maximumSize(1024)
        .expireAfterAccess(10L, TimeUnit.MINUTES).removalListener(removalNotification -> { })
        .build();


    @Resource
    JdbcTemplate jdbcTemplate;
    private final ExpressionParser parser = new SpelExpressionParser();
    @Resource
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<User> page(int index, int pageSize, String order, String where, Object... param) {
        String limit = MessageFormat.format("LIMIT {0} OFFSET {1}", pageSize, pageSize * index);
        String sql = "SELECT uid,\nname,\nusername,\npassword,\nnick_name,\nmobile_phone,\nemail_address,\ntype,\ngender,\nlanguage,\ntime_zone,\navatar_uri,\nbirthday,\nis_lunar_birthday,\ngrade,\ncountry,\nprovince,\ncity,\ndistrict,\nsalt,\nlogin_ip,\nlogin_ts,\nlogin_failed,\npassword_expires_in,\nis_locked,\nlocked_time,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user " +
             where +
             order +
             limit;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }


    public List<User> query(String where, Object... param) {
        String sql = "SELECT uid,\nname,\nusername,\npassword,\nnick_name,\nmobile_phone,\nemail_address,\ntype,\ngender,\nlanguage,\ntime_zone,\navatar_uri,\nbirthday,\nis_lunar_birthday,\ngrade,\ncountry,\nprovince,\ncity,\ndistrict,\nsalt,\nlogin_ip,\nlogin_ts,\nlogin_failed,\npassword_expires_in,\nis_locked,\nlocked_time,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user " + where;
        return this.jdbcTemplate.query(sql, this::extractData, param);
    }

    public List<User> queryByPrimaryKey(Object key) {
        String sql = "SELECT uid,\nname,\nusername,\npassword,\nnick_name,\nmobile_phone,\nemail_address,\ntype,\ngender,\nlanguage,\ntime_zone,\navatar_uri,\nbirthday,\nis_lunar_birthday,\ngrade,\ncountry,\nprovince,\ncity,\ndistrict,\nsalt,\nlogin_ip,\nlogin_ts,\nlogin_failed,\npassword_expires_in,\nis_locked,\nlocked_time,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\nFROM t_user  WHERE uid=? \n;";
        return this.jdbcTemplate.query(sql, this::extractData, key);
    }

    public int insert(User... entities) {
        String sql = "INSERT INTO t_user (uid,\nname,\nusername,\npassword,\nnick_name,\nmobile_phone,\nemail_address,\ntype,\ngender,\nlanguage,\ntime_zone,\navatar_uri,\nbirthday,\nis_lunar_birthday,\ngrade,\ncountry,\nprovince,\ncity,\ndistrict,\nsalt,\nlogin_ip,\nlogin_ts,\nlogin_failed,\npassword_expires_in,\nis_locked,\nlocked_time,\ncreate_time,\nupdate_time,\ncreate_by,\nupdate_by,\nstatus\n) VALUES (\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?,\n?\n)";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var name = entity.getName();
            var username = entity.getUsername();
            var password = entity.getPassword();
            var nickName = entity.getNickName();
            var mobilePhone = entity.getMobilePhone();
            var emailAddress = entity.getEmailAddress();
            var type = entity.getType();
            var gender = entity.getGender();
            var language = entity.getLanguage();
            var timeZone = entity.getTimeZone();
            var avatarUri = entity.getAvatarUri();
            var birthday = entity.getBirthday();
            var isLunarBirthday = entity.getIsLunarBirthday();
            var grade = entity.getGrade();
            var country = entity.getCountry();
            var province = entity.getProvince();
            var city = entity.getCity();
            var district = entity.getDistrict();
            var salt = entity.getSalt();
            var loginIp = entity.getLoginIp();
            var loginTime = entity.getLoginTime();
            var loginFailedTimes = entity.getLoginFailedTimes();
            var passwordExpiresIn = entity.getPasswordExpiresIn();
            var isLocked = entity.getIsLocked();
            var lockedTime = entity.getLockedTime();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, id,name,username,password,nickName,mobilePhone,emailAddress,type,gender,language,timeZone,avatarUri,birthday,isLunarBirthday,grade,country,province,city,district,salt,loginIp,loginTime,loginFailedTimes,passwordExpiresIn,isLocked,lockedTime,createdTime,updatedTime,createdBy,updatedBy,status);
        }
        return result;
    }

    public int update(User... entities) {

        String sql = "UPDATE t_user SET name=?,\nusername=?,\npassword=?,\nnick_name=?,\nmobile_phone=?,\nemail_address=?,\ntype=?,\ngender=?,\nlanguage=?,\ntime_zone=?,\navatar_uri=?,\nbirthday=?,\nis_lunar_birthday=?,\ngrade=?,\ncountry=?,\nprovince=?,\ncity=?,\ndistrict=?,\nsalt=?,\nlogin_ip=?,\nlogin_ts=?,\nlogin_failed=?,\npassword_expires_in=?,\nis_locked=?,\nlocked_time=?,\ncreate_time=?,\nupdate_time=?,\ncreate_by=?,\nupdate_by=?,\nstatus=?\n WHERE uid=? \n;";
        int result = 0;
        for (var entity : entities) {
            var id = entity.getId();
            var name = entity.getName();
            var username = entity.getUsername();
            var password = entity.getPassword();
            var nickName = entity.getNickName();
            var mobilePhone = entity.getMobilePhone();
            var emailAddress = entity.getEmailAddress();
            var type = entity.getType();
            var gender = entity.getGender();
            var language = entity.getLanguage();
            var timeZone = entity.getTimeZone();
            var avatarUri = entity.getAvatarUri();
            var birthday = entity.getBirthday();
            var isLunarBirthday = entity.getIsLunarBirthday();
            var grade = entity.getGrade();
            var country = entity.getCountry();
            var province = entity.getProvince();
            var city = entity.getCity();
            var district = entity.getDistrict();
            var salt = entity.getSalt();
            var loginIp = entity.getLoginIp();
            var loginTime = entity.getLoginTime();
            var loginFailedTimes = entity.getLoginFailedTimes();
            var passwordExpiresIn = entity.getPasswordExpiresIn();
            var isLocked = entity.getIsLocked();
            var lockedTime = entity.getLockedTime();
            var createdTime = entity.getCreatedTime();
            var updatedTime = entity.getUpdatedTime();
            var createdBy = entity.getCreatedBy();
            var updatedBy = entity.getUpdatedBy();
            var status = entity.getStatus();
            result += this.jdbcTemplate.update( sql, name,username,password,nickName,mobilePhone,emailAddress,type,gender,language,timeZone,avatarUri,birthday,isLunarBirthday,grade,country,province,city,district,salt,loginIp,loginTime,loginFailedTimes,passwordExpiresIn,isLocked,lockedTime,createdTime,updatedTime,createdBy,updatedBy,status,id);
        }
        return result;
    }

    public int delete(int... ids) {
        String sql = "UPDATE t_user SET status = -1 WHERE uid=? \n;";
        int result = 0;
        for (var id : ids) {
            result += this.jdbcTemplate.update(sql, id);
        }
        return result;
    }

    public int deleteAll(Integer... ids) {
        String sql = "DELETE FROM t_user WHERE uid=? \n;";
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

    List<User> extractData(ResultSet rs)  {
        List<User> list = new ArrayList<>();
		try {
            while (rs.next()) {
                User entity = new User();
                entity.setId(rs.getLong("uid"));
                entity.setName(rs.getString("name"));
                entity.setUsername(rs.getString("username"));
                entity.setPassword(rs.getString("password"));
                entity.setNickName(rs.getString("nick_name"));
                entity.setMobilePhone(rs.getString("mobile_phone"));
                entity.setEmailAddress(rs.getString("email_address"));
                // todo: UserType ;
                // todo: GenderType ;
                entity.setLanguage(rs.getString("language"));
                entity.setTimeZone(rs.getInt("time_zone"));
                entity.setAvatarUri(rs.getString("avatar_uri"));
                entity.setBirthday(rs.getDate("birthday"));
                // todo: Boolean ;
                // todo: GradeType ;
                entity.setCountry(rs.getString("country"));
                entity.setProvince(rs.getString("province"));
                entity.setCity(rs.getString("city"));
                entity.setDistrict(rs.getString("district"));
                entity.setSalt(rs.getString("salt"));
                entity.setLoginIp(rs.getString("login_ip"));
                entity.setLoginTime(rs.getDate("login_ts"));
                entity.setLoginFailedTimes(rs.getInt("login_failed"));
                entity.setPasswordExpiresIn(rs.getInt("password_expires_in"));
                // todo: Boolean ;
                entity.setLockedTime(rs.getDate("locked_time"));
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
