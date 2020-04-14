package ltd.fdsa.starter.logger.util;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class TraceUtil {

	public static String getTrace() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		
		String app_trace_id = request.getHeader("TRACE_ID");
		
		//未经过HandlerInterceptor的设置 
//		if (StringUtils.isBlank(MDC.get(TraceConstant.LOG_TRACE_ID))) {
//			//但是有请求头，重新设置
//			if (StringUtils.isNotEmpty(app_trace_id)) {
//				MDC.put(TraceConstant.LOG_TRACE_ID, app_trace_id);
//			}
//		}

		return app_trace_id;

	}

}
