package ltd.fdsa.database.controller;


import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BaseController {
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    protected String getUserId() {
        return this.request.getHeader("X-USERID");
    }

    protected String[] getUserRoles() {
        return this.request.getHeader("X-USER-ROLES").split(",");
    }

}