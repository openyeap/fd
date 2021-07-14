package ltd.fdsa.job.admin.jpa.service;

import ltd.fdsa.database.service.DataAccessService;
import ltd.fdsa.job.admin.jpa.entity.SystemUser;
import ltd.fdsa.web.view.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SystemUserService extends DataAccessService<SystemUser, Integer> {
    public static final String USER_LOGIN_IDENTITY = "USER_LOGIN_IDENTITY";

    SystemUser checkLogin(HttpServletRequest request, HttpServletResponse response);

    Result<String> login(String username, String password);

    SystemUser loadByUserName(String username);
}
