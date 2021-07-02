package ltd.fdsa.job.admin.resolver;

import ltd.fdsa.switcher.core.exception.FastDataSwitchException;
import ltd.fdsa.switcher.core.util.JacksonUtil;
import ltd.fdsa.web.view.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * common exception resolver
 */
@Component
public class WebExceptionResolver implements HandlerExceptionResolver {
    private static transient Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        if (!(ex instanceof FastDataSwitchException)) {
            logger.error("WebExceptionResolver:{}", ex);
        }

        // if json
        boolean isJson = false;
        HandlerMethod method = (HandlerMethod) handler;
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        if (responseBody != null) {
            isJson = true;
        }

        // error result
        Result<String> errorResult = Result.fail(500, ex.toString().replaceAll("\n", "<br/>"));

        // response
        ModelAndView mv = new ModelAndView();
        if (isJson) {
            try {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print(JacksonUtil.writeValueAsString(errorResult));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            return mv;
        } else {

            mv.addObject("exceptionMsg", errorResult.getMessage());
            mv.setViewName("/common/common.exception");
            return mv;
        }
    }
}
