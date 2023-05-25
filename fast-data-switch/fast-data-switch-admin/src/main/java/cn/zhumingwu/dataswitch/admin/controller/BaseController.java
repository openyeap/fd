package cn.zhumingwu.dataswitch.admin.controller;

import cn.zhumingwu.dataswitch.admin.config.WebMvcConfig;
import org.springframework.beans.propertyeditors.CustomDateEditor;
 
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseController {
 

    protected String getUserId() {
        return WebMvcConfig.getRequest().block().getRequest().getHeaders().getFirst("X-USERID");
    }

    protected String[] getUserRoles() {
        return WebMvcConfig.getRequest().block().getRequest().getHeaders().getFirst("X-USER-ROLES").split(",");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
