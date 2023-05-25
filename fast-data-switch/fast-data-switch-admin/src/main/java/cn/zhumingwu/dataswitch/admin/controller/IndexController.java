package cn.zhumingwu.dataswitch.admin.controller;

import lombok.var;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.admin.service.impl.SystemUserService;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.Map;


/**
 * index controller
 */
@Controller
public class IndexController extends BaseController {


    @Resource
    private SystemUserService loginService;

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {

//        Map<String, Object> dashboardMap = JobService.dashboardInfo();
//        model.addAllAttributes(dashboardMap);
        return "index";
    }

    @RequestMapping("/chartInfo")
    @ResponseBody
    public Result<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
//        Result<Map<String, Object>> chartInfo = JobService.chartInfo(startDate, endDate);
//        return chartInfo;
        return null;
    }

    @RequestMapping("/login")
//    @PermissionLimit(limit = false)
    public String toLogin() {
        if (loginService.checkLogin() != null) {
            return "redirect:/";
        }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
//    @PermissionLimit(limit = false)
    public Result<String> doLogin(String username, String password, String remember) {
        var result = loginService.login(username, password);
        if (result.getCode() == 200 || result.getCode() == 0) {
            var cookie = ResponseCookie.from("UID", result.getData()).httpOnly(true).maxAge(Duration.ofDays(365)).build();
//            response.addCookie(cookie);
        }
        return result;
    }

    @RequestMapping(value = "logout", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
//    @PermissionLimit(limit = false)
    public Result<String> logout() {
        var cookie = ResponseCookie.from("UID", "").httpOnly(true).maxAge(Duration.ofDays(-1)).build();
//        response.addCookie(cookie);
        return Result.success();
    }

    @RequestMapping("/help")
    public String help() {
        return "help";
    }

}
