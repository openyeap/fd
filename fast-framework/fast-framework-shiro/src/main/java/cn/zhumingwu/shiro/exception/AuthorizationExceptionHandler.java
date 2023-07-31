package cn.zhumingwu.shiro.exception;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.base.model.Result;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



import java.io.IOException;

/**
 * 拦截访问权限异常处理
 */
@ControllerAdvice
@Order(-1)
public class AuthorizationExceptionHandler {

    /**
     * 拦截访问权限异常
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Result<Object> authorizationException(AuthorizationException e, HttpServletRequest request, HttpServletResponse response) {

        // 获取异常信息
        Throwable cause = e.getCause();
        String message = cause.getMessage();

        // 判断无权限访问的方法返回对象是否为ResultVo

        try {
            // 重定向到无权限页面
            String contextPath = request.getContextPath();
            ShiroFilterFactoryBean shiroFilter = ApplicationContextHolder.getBean(ShiroFilterFactoryBean.class);
            response.sendRedirect(contextPath + shiroFilter.getUnauthorizedUrl());
        } catch (IOException ex) {
            return Result.fail(HttpCode.UNAUTHORIZED);
        }

        return Result.fail(HttpCode.UNAUTHORIZED);
    }
}
