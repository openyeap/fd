package ltd.fdsa.job.admin.jpa.service.impl;

import com.google.common.base.Strings;
import lombok.var;
import ltd.fdsa.core.util.Base64Utils;
import ltd.fdsa.database.entity.Status;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.SystemUser;
import ltd.fdsa.job.admin.jpa.repository.reader.SystemUserReader;
import ltd.fdsa.job.admin.jpa.repository.writer.SystemUserWriter;
import ltd.fdsa.job.admin.jpa.service.SystemUserService;
import ltd.fdsa.job.admin.util.CookieUtil;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.web.view.Result;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class SystemUserServiceImpl extends BaseJpaService<SystemUser, Integer, SystemUserWriter, SystemUserReader> implements SystemUserService {


    @Override
    public SystemUser loadByUserName(String username) {
        var userQuery = new SystemUser();
        userQuery.setName(username);
        userQuery.setStatus(Status.OK);
        var example = Example.of(userQuery);
        var result = this.reader.findOne(example);
        if (!result.isPresent()) {
            return null;
        }
        return result.get();
    }

    private String makeToken(SystemUser systemUser) {
        var version = "v1";
        var id = Base64Utils.urlEncode(systemUser.getId() + ":" + "role1,role2");
        var kv = Base64Utils.urlEncode(MessageFormat.format("name:{0},email:{1}", systemUser.getName(), systemUser.getEmail()));
        var timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        var signature = Base64Utils.sum(version + id + kv + timestamp);
        String token = version + "." + id + "." + kv + "." + timestamp + "." + signature;
        return token;
    }

    private SystemUser parseToken(String token) {
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
        var sum = Base64Utils.sum(version + id + kv + timestamp);
        if (!signature.equals(sum + "")) {
            return null;
        }
        var data = new LinkedHashMap<String, String>();

        for (var m : Base64Utils.urlDecode(kv).split(",")) {
            var i = m.split("\\:");
            data.put(i[0], i[1]);
        }
        var user = new SystemUser();
        user.setId(Integer.valueOf(Base64Utils.urlDecode(id).split(":")[0]));
        user.setName(data.getOrDefault("name", ""));
        user.setEmail(data.getOrDefault("email", ""));
        return user;
    }

    @Override
    public Result<String> login(String username, String password) {

        // param
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            return Result.fail(500, I18nUtil.getString("login_param_empty"));
        }

        // valid passowrd
        SystemUser user = this.loadByUserName(username);
        if (user == null) {
            return Result.fail(500, I18nUtil.getString("login_param_invalid"));
        }

        String passwordMd5 = DigestUtils.md5DigestAsHex((user.getSalt() + password).getBytes());
        if (!passwordMd5.equals(user.getPassword())) {
            return Result.fail(500, I18nUtil.getString("login_param_invalid"));
        }

        String loginToken = makeToken(user);
        return Result.success(loginToken);
    }


    /**
     * logout
     *
     * @param request
     * @return
     */

    @Override
    public SystemUser checkLogin(HttpServletRequest request, HttpServletResponse response) {
        String token = getTokenFromCookie(request);
        if (Strings.isNullOrEmpty(token)) {
            token = getTokenFromHeader(request);
        }
        if (Strings.isNullOrEmpty(token)) {
            token = getTokenFromQuery(request);
        }
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        SystemUser user = parseToken(token);
        return user;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        return CookieUtil.getValue(request, SystemUserService.USER_LOGIN_IDENTITY);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        var bearer = request.getHeader("Authorization");
        if (Strings.isNullOrEmpty(bearer) || !bearer.startsWith("Bearer")) {
            return null;
        }
        return bearer.substring(7);
    }

    private String getTokenFromQuery(HttpServletRequest request) {
        return request.getParameter("access_token");
    }
}
