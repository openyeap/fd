package cn.zhumingwu.client.service;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.base.token.*;
import cn.zhumingwu.base.util.EncryptUtil;
import cn.zhumingwu.client.entity.User;
import cn.zhumingwu.client.repository.UserRepository;

import cn.zhumingwu.client.view.Token;
import jakarta.annotation.Resource;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.util.Assert;

import java.sql.ResultSet;

@Service
public class UserServiceImpl extends UserService {
    @Resource(name = "dis")
    Cache cache;
    @Resource
    SimpleWebTokenValidation validation;
    @Resource
    SimpleWebTokenGenerator generator;

    public Result<SimpleWebToken> login(String username, String password) {
        var user = this.getUserByUsername(username);
        if (user.getCode() == -1) {
            return Result.none();
        }
        var passwordHash = EncryptUtil.encrypt(password, user.getData().getSalt());
        if (Objects.equals(passwordHash, user.getData().getPassword())) {
            var swt = generator.generate(user.getData().getId().toString(), "cid", new HashMap<>());
            return Result.success(swt);
        }
        return Result.none();
    }

    public Result<SimpleWebToken> refreshToken(String refreshToken, String password) {
        if (this.cache!=null &&  this.cache.get(refreshToken) != null) {
            return Result.none();
        }
        var swt = this.generator.refresh(refreshToken);
        if (swt == null) {
            return Result.none();
        }
        return Result.success(swt);
    }

    Result<User> getUserByUsername(String username) {
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("username", username);
        var sql = "select id, password, salt from t_user where username = :username and status=1";
        return this.repos.getNamedQuery(sql, (ResultSet rs) -> {
            Assert.notNull(rs, "ResultSet must not be null");
            if (rs.next()) {
                var user = new User();
                user.setId(rs.getLong("id"));
                user.setSalt(rs.getString("salt"));
                user.setPassword(rs.getString("password"));
                return Result.success(user);
            }
            return Result.none();
        }, paramMap);

//        Map<String, Object> paramMap = new LinkedHashMap<>();
//        paramMap.put("name", name);
//        return this.repos.getExpQuery("select * from users where name = #name", (ResultSet rs) -> {
//            Assert.notNull(rs, "ResultSet must not be null");
//            List<User> list = new ArrayList<User>();
//            while (rs.next()) {
//                var user = new User();
//                user.setId(rs.getLong("id"));
//                user.setUsername(rs.getString("username"));
//                user.setEmailAddress(rs.getString("email"));
//                user.setMobilePhone(rs.getString("phone_number"));
//                user.setPassword(rs.getString("password"));
//                list.add(user);
//            }
//            return list;
//        }, paramMap);
    }

    public User getUserById(long id) {
        return this.repos.getQuery("select * from users where id =?", rs -> {
            Assert.notNull(rs, "ResultSet must not be null");
            if (rs.next()) {
                var user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmailAddress(rs.getString("email"));
                user.setMobilePhone(rs.getString("phone_number"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            return null;
        }, id);
    }

    public User getUserByName(String name) {
        return this.repos.getQuery("select * from users where name =?", rs -> {
            Assert.notNull(rs, "ResultSet must not be null");
            if (rs.next()) {
                var user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmailAddress(rs.getString("email"));
                user.setMobilePhone(rs.getString("phone_number"));
                user.setPassword(rs.getString("password"));
                return user;
            }
            return null;
        }, name);
    }

    public User validate(String token) {
        var result = validation.validate(token);
        if (result == SimpleWebTokenValidation.ValidationResult.TOKEN_VALID) {
            var rs = result.getClaims();

            var user = new User();
//        user.setId(rs.getLong("id"));
            user.setUsername(rs.get("username"));
            user.setEmailAddress(rs.get("email"));
            user.setMobilePhone(rs.get("phone_number"));
            user.setPassword(rs.get("password"));
            return user;
        }
        return null;
    }

    public User refresh(String refreshToken) {
        var result = generator.refresh(refreshToken);
        if (result == null) {
            return null;
        }
        var rs = result.getClaims();
        var user = new User();
        user.setUsername(rs.get("username"));
        user.setEmailAddress(rs.get("email"));
        user.setMobilePhone(rs.get("phone_number"));
        user.setPassword(rs.get("password"));
        return user;
    }

}