package cn.zhumingwu.dataswitch.admin.service.impl;

import cn.zhumingwu.dataswitch.admin.repository.SystemUserRepository;
import com.google.common.base.Strings;
import lombok.var;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.HashUtils;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;
import cn.zhumingwu.dataswitch.admin.config.WebMvcConfig;
import cn.zhumingwu.dataswitch.admin.entity.BaseEntity;
import cn.zhumingwu.dataswitch.admin.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;


import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;

@Service
public class SystemUserService {

    @Resource
    private SystemUserRepository reader;


    public User loadByUserName(String username) {
        var userQuery = new User();
        userQuery.setName(username);
        userQuery.setStatus(BaseEntity.Status.OK);
        var example = Example.of(userQuery);
        var result = this.reader.findOne(example);
        if (!result.isPresent()) {
            return null;
        }
        return result.get();
    }

    private String makeToken(User systemUser) {
        var version = "v1";
        var id = Base64Utils.encodeToString(systemUser.getId().toString().getBytes(StandardCharsets.UTF_8));
        var kv = Base64Utils.encodeToString(MessageFormat.format("name:{0},email:{1}", systemUser.getName(), systemUser.getEmail()).getBytes(StandardCharsets.UTF_8));
        var timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        var signature = HashUtils.hash((version + id + kv + timestamp).getBytes(StandardCharsets.UTF_8));
        String token = version + "." + id + "." + kv + "." + timestamp + "." + signature;
        return token;
    }

    private User parseToken(String token) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        var items = token.split("\\.");
        if (items.length != 5) {
            return null;
        }
        var version = items[0];
        if (!"v1".equals(version)) {
            return null;
        }

        var id = items[1];
        var kv = items[2];
        var timestamp = items[3];
        var signature = items[4];
        var sum = HashUtils.hash((version + id + kv + timestamp).getBytes(StandardCharsets.UTF_8));
        if (!signature.equals(sum + "")) {
            return null;
        }
        var data = new LinkedHashMap<String, String>();

        var user = new User();
        user.setName(data.getOrDefault("name", ""));
        user.setEmail(data.getOrDefault("email", ""));
        return user;
    }

    public Result<String> login(String username, String password) {

        // param
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            return Result.fail(500, I18nUtil.getInstance("").getString("login_param_empty"));
        }

        // valid passowrd
        User user = this.loadByUserName(username);
        if (user == null) {
            return Result.fail(500, I18nUtil.getInstance("").getString("login_param_invalid"));
        }

        String passwordMd5 = DigestUtils.md5DigestAsHex((user.getSalt() + password).getBytes());
        if (!passwordMd5.equals(user.getPassword())) {
            return Result.fail(500, I18nUtil.getInstance("").getString("login_param_invalid"));
        }

        String loginToken = makeToken(user);
        return Result.success(loginToken);
    }

    public User checkLogin() {
        String token = getTokenFromCookie();
        if (Strings.isNullOrEmpty(token)) {
            token = getTokenFromHeader();
        }
        if (Strings.isNullOrEmpty(token)) {
            token = getTokenFromQuery();
        }
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        User user = parseToken(token);
        return user;
    }

    private String getTokenFromCookie() {

        var cookie = WebMvcConfig.getRequest().block().getRequest().getCookies().getFirst("UID");
        if (cookie != null) {
            return cookie.getValue();
        }
        return "";
    }

    private String getTokenFromHeader() {
        var bearer = WebMvcConfig.getRequest().block().getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (Strings.isNullOrEmpty(bearer) || !bearer.startsWith("Bearer ")) {
            return null;
        }
        return bearer.substring(7);
    }

    private String getTokenFromQuery() {
        return WebMvcConfig.getRequest().block().getRequest().getQueryParams().getFirst("access_token");
    }

    public void update(User systemUser) {
        this.reader.save(systemUser);
    }

    public void deleteById(int id) {
        this.reader.deleteById(id);
    }
}
