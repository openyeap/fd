package ltd.fdsa.job.admin.jpa.service.impl;

import com.google.common.base.Strings;
import lombok.var;
import ltd.fdsa.core.util.Base64Utils;
import ltd.fdsa.core.util.LicenseUtils;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemUserServiceImpl extends BaseJpaService<SystemUser, Integer, SystemUserWriter, SystemUserReader> implements SystemUserService {


    @Override
    public SystemUser loadByUserName(String username) {
        var e = new SystemUser();
        e.setUsername(username);
        var example = Example.of(e);
        var u = this.reader.findOne(example);
        if (!u.isPresent()) {
            return null;
        }
        return u.get();
    }

    private String makeToken(SystemUser JobUser) {
        var version = "v1";
        var id = JobUser.getId() + ":" + "role1,role2";
        var kv = "k1:v1,k2:v2";
        var timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        var signature = LicenseUtils.CheckSum(version + id + kv + timestamp);
        String token = Base64Utils.encode(version) + "." + Base64Utils.encode(id) + "." + Base64Utils.encode(kv) + "." + timestamp + "." + signature;
        return token;
    }

    private SystemUser parseToken(String token) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        var items = token.split(".");
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
        var checkSum = LicenseUtils.CheckSum(version + id + kv + timestamp);
        if (!signature.equals(checkSum)) {
            return null;
        }
        var data = Arrays.stream(kv.split(",")).map(m -> {
            var i = m.split(":");
            return new AbstractMap.SimpleEntry<String, String>(i[0], i[1]);
        }).collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue(), (oldVal, currVal) -> oldVal, LinkedHashMap::new));
        var user = new SystemUser();
        user.setId(Integer.valueOf(id.split(":")[0]));
        user.setUsername(data.getOrDefault("username", ""));
        user.setEmailAddress(data.getOrDefault("email_address", ""));
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
    public SystemUser ifLogin(HttpServletRequest request, HttpServletResponse response) {
        String cookieToken = CookieUtil.getValue(request, SystemUserService.USER_LOGIN_IDENTITY);
        if (Strings.isNullOrEmpty(cookieToken)) {
            return null;
        }

        SystemUser user = parseToken(cookieToken);
        if (user == null) {
            return null;
        }
        return user;
    }
}
