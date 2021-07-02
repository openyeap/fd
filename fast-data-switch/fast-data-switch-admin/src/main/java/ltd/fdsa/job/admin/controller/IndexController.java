package ltd.fdsa.job.admin.controller;

import ltd.fdsa.job.admin.annotation.PermissionLimit;
import ltd.fdsa.job.admin.jpa.service.impl.JobUserServiceImpl;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 */
@Controller
public class IndexController {


    @Resource
    private JobUserServiceImpl loginService;

    @RequestMapping("/")
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

    @RequestMapping("/toLogin")
    @PermissionLimit(limit = false)
    public String toLogin(HttpServletRequest request, HttpServletResponse response) {
        if (loginService.ifLogin(request, response) != null) {
            return "redirect:/";
        }
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    @PermissionLimit(limit = false)
    public Result<String> loginDo(
            HttpServletRequest request,
            HttpServletResponse response,
            String userName,
            String password,
            String ifRemember) {
        boolean ifRem =
                (ifRemember != null && ifRemember.trim().length() > 0 && "on".equals(ifRemember))
                        ? true
                        : false;
        return loginService.login(request, response, userName, password, ifRem);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    @PermissionLimit(limit = false)
    public Result<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return loginService.logout(request, response);
    }

    @RequestMapping("/help")
    public String help() {

    /*if (!PermissionInterceptor.ifLogin(request)) {
    	return "redirect:/toLogin";
    }*/

        return "help";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
