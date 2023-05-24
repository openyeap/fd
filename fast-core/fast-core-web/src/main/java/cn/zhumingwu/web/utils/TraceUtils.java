package cn.zhumingwu.web.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.var;
import cn.zhumingwu.base.util.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;


public class TraceUtils {
    public final static String TRACE_ID = "TRACE_ID";
    private final static String SPAN_ID = "SPAN_ID";
    private final static String SPAN_TIME = "SPAN_TIME";
    private final static String SPAN_METHOD = "SPAN_METHOD";
    private final static String SPAN_URL = "SPAN_URL";
    private final static String SPEND_TIME = "SPEND_TIME";
    private final static IdUtils ID_INSTANCE = IdUtils.getInstance();
    private static final Logger log = LoggerFactory.getLogger(TRACE_ID);

    public static String startTrace(HttpServletRequest... requests) {
        HttpServletRequest request;
        if (requests.length <= 0) {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            request = sra.getRequest();
        } else {
            request = requests[0];
        }
        String traceId = request.getHeader(TRACE_ID);
        if (Strings.isNullOrEmpty(traceId)) {
            traceId = UUID.randomUUID().toString();
            MDC.put(TRACE_ID, traceId);
        }
        MDC.put(SPAN_ID, Long.toString(ID_INSTANCE.nextId()));
        MDC.put(SPAN_TIME, Long.toString(ID_INSTANCE.now()));
        MDC.put(SPAN_METHOD, request.getMethod());
        MDC.put(SPAN_URL, request.getRequestURI());
        return traceId;
    }

    public static String getTraceId(HttpServletRequest... requests) {
        String traceId = MDC.get(TRACE_ID);
        if (!Strings.isNullOrEmpty(traceId)) {
            return traceId;
        }
        return startTrace(requests);
    }

    public static void endStrace(Map<String, String> meta) {
        var data = Maps.newLinkedHashMap(MDC.getCopyOfContextMap());
        data.putAll(meta);
        var time = Long.parseLong(MDC.get(SPAN_TIME));
        time = ID_INSTANCE.now() - time;
        data.put(SPEND_TIME, Long.toString(time));
        MDC.put(SPEND_TIME, Long.toString(time));
        log.trace("{}", data);
        //MDC.remove(TRACE_ID);
        MDC.clear();
    }
}
