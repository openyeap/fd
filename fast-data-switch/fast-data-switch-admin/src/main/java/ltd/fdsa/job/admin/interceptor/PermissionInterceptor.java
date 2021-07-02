package ltd.fdsa.job.admin.interceptor;

import ltd.fdsa.job.admin.annotation.PermissionLimit;
import ltd.fdsa.job.admin.jpa.entity.JobUser;
import ltd.fdsa.job.admin.jpa.service.impl.JobUserServiceImpl;
import ltd.fdsa.job.admin.util.I18nUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private JobUserServiceImpl loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        // if need login
        boolean needLogin = true;
        boolean needAdminuser = false;
        HandlerMethod method = (HandlerMethod) handler;
        PermissionLimit permission = method.getMethodAnnotation(PermissionLimit.class);
        if (permission != null) {
            needLogin = permission.limit();
            needAdminuser = permission.adminuser();
        }

        if (needLogin) {
            JobUser loginUser = loginService.ifLogin(request, response);
            if (loginUser == null) {
                response.sendRedirect(request.getContextPath() + "/toLogin");
                // request.getRequestDispatcher("/toLogin").forward(request, response);
                return false;
            }
            if (needAdminuser && loginUser.getRole() != 1) {
                throw new RuntimeException(I18nUtil.getString("system_permission_limit"));
            }
            request.setAttribute(JobUserServiceImpl.LOGIN_IDENTITY_KEY, loginUser);
        }

        return super.preHandle(request, response, handler);
    }
}
