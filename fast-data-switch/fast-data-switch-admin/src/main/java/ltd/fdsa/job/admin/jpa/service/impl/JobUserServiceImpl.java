package ltd.fdsa.job.admin.jpa.service.impl;

import lombok.var;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.JobUser;
import ltd.fdsa.job.admin.jpa.repository.reader.JobUserReader;
import ltd.fdsa.job.admin.jpa.repository.writer.JobUserWriter;
import ltd.fdsa.job.admin.jpa.service.JobUserService;
import ltd.fdsa.job.admin.util.CookieUtil;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.switcher.core.util.JacksonUtil;
import ltd.fdsa.web.view.Result;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@Service
public class JobUserServiceImpl extends BaseJpaService<JobUser, Integer, JobUserWriter, JobUserReader> implements JobUserService {

    public static final String LOGIN_IDENTITY_KEY = "JOB_LOGIN_IDENTITY";
    @Override
    public JobUser loadByUserName(String userName) {
        return null;
    }

    private String makeToken(JobUser JobUser) {
        String tokenJson = JacksonUtil.writeValueAsString(JobUser);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }

    private JobUser parseToken(String tokenHex) {
        JobUser JobUser = null;
        if (tokenHex != null) {
            String tokenJson =
                    new String(new BigInteger(tokenHex, 16).toByteArray()); // username_password(md5)
            JobUser = JacksonUtil.readValue(tokenJson, JobUser.class);
        }
        return JobUser;
    }

    public Result<String> login(            HttpServletRequest request,            HttpServletResponse response,            String username,            String password,            boolean ifRemember) {

        // param
        if (username == null
                || username.trim().length() == 0
                || password == null
                || password.trim().length() == 0) {
            return Result.fail(500, I18nUtil.getString("login_param_empty"));
        }

        // valid passowrd
        var e = new JobUser();
        e.setUsername(username);
        var example = Example.of(e);
       var u =  this.reader.findOne(example);
        if (!u.isPresent())
        {
            e.setPassword("qwe123!@#");
            this.writer.save(e) ;
        }
        JobUser JobUser = this.reader.findOne(example).get();
        if (JobUser == null) {
            return Result.fail(500, I18nUtil.getString("login_param_unvalid"));
        }
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwordMd5.equals(JobUser.getPassword())) {
            return Result.fail(500, I18nUtil.getString("login_param_unvalid"));
        }

        String loginToken = makeToken(JobUser);

        // do login
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, loginToken, ifRemember);
        return Result.success();
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public Result<String> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return Result.success();
    }

    /**
     * logout
     *
     * @param request
     * @return
     */
    public JobUser ifLogin(HttpServletRequest request, HttpServletResponse response) {
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (cookieToken != null) {
            JobUser cookieUser = null;
            try {
                cookieUser = parseToken(cookieToken);
            } catch (Exception e) {
                logout(request, response);
            }
            if (cookieUser != null) {
                var e = new JobUser();
                e.setUsername(cookieUser.getUsername());
                var example = Example.of(e);
                JobUser dbUser = this.reader.findOne(example).get();
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }


}
