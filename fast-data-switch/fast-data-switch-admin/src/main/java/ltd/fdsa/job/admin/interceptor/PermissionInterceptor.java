package ltd.fdsa.job.admin.interceptor;

import ltd.fdsa.job.admin.annotation.PermissionLimit;
import ltd.fdsa.job.admin.jpa.entity.SystemUser;
import ltd.fdsa.job.admin.jpa.service.SystemUserService;
import ltd.fdsa.job.admin.util.FtlUtil;
import ltd.fdsa.job.admin.util.I18nUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 权限拦截
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private SystemUserService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }
        HandlerMethod method = (HandlerMethod) handler;
        // if need login
        boolean needLogin = true;
        boolean needAdminister = false;
        PermissionLimit permission = method.getMethodAnnotation(PermissionLimit.class);
        if (permission != null) {
            needLogin = permission.limit();
            needAdminister = permission.adminuser();
        }

        if (needLogin) {
            SystemUser loginUser = loginService.checkLogin(request, response);
            if (loginUser == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
            if (needAdminister && loginUser.getType() != 1) {
                throw new RuntimeException(I18nUtil.getString("system_permission_limit"));
            }
            request.setAttribute(SystemUserService.USER_LOGIN_IDENTITY, loginUser);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

        // cookie
        if (modelAndView != null && request.getCookies() != null && request.getCookies().length > 0) {
            HashMap<String, Cookie> cookieMap = new HashMap<String, Cookie>();
            for (Cookie ck : request.getCookies()) {
                cookieMap.put(ck.getName(), ck);
            }
            modelAndView.addObject("cookieMap", cookieMap);
        }

        // static method
        if (modelAndView != null) {
            modelAndView.addObject("I18nUtil", FtlUtil.generateStaticModel(I18nUtil.class.getName()));
        }

        super.postHandle(request, response, handler, modelAndView);
    }
}
