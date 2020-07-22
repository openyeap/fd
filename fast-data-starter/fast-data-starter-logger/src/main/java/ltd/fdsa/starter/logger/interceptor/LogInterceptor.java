package ltd.fdsa.starter.logger.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class LogInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String traceId = request.getHeader("TRACE_ID");
		if (!StringUtils.isEmpty(traceId)) {
			MDC.put("TRACE_ID", traceId);
		}
		MDC.put("TRACE_ID", "traceId");
		return true;
	}
}