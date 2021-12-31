package ltd.fdsa.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}