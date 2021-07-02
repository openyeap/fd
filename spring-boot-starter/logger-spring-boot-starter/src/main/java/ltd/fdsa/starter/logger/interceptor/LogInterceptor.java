package ltd.fdsa.starter.logger.interceptor;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.web.utils.TraceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Value("${spring.application.name:default}")
    private String name;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TraceUtils.startTrace(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Map<String, String> meta = new LinkedHashMap<>();
        meta.put("APP_NAME", this.name);
        TraceUtils.endStrace(meta);
    }
}
