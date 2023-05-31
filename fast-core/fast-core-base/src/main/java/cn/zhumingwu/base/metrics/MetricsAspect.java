package cn.zhumingwu.base.metrics;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.serializer.Serializer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
@Slf4j
public class MetricsAspect {
    private static Metrics DEFAULT_METRICS;

    static {
        // 默认注解
        // 通过在一个内部类上定义@Metrics注解方式，然后通过反射获取注解的小技巧来获得一个默认的@Metrics注解的实例
        @Metrics
        final class c {
        }
        DEFAULT_METRICS = c.class.getAnnotation(Metrics.class);
    }

    @Autowired(required = false)
    Serializer<Object> serializer;

    @Pointcut("within(@Metrics *)")
    public void withMetricsAnnotationClass() {
    }

    // @annotation指示器实现对标记了Metrics注解的方法进行匹配
    @Pointcut("@annotation(Metrics)")
    public void annotationMetricsAnnotationMethod() {
    }

    // within指示器实现了匹配那些类型上标记了@RestController注解的方法
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerBean() {
    }

    @Around("controllerBean() || annotationMetricsAnnotationMethod() || withMetricsAnnotationClass()")
    public Object metrics(ProceedingJoinPoint joinPoint) throws Throwable {
        // 通过连接点获取方法签名和方法上Metrics注解，并根据方法签名生成日志中要输出的方法定义描述
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Metrics metrics = signature.getMethod().getAnnotation(Metrics.class);
        if (metrics == null) {
            metrics = joinPoint.getTarget().getClass().getAnnotation((Metrics.class));
        }
        if (metrics == null) {
            metrics = DEFAULT_METRICS;
        }
        String name = String.format("【%s】【%s】", signature.getDeclaringType().toString(), signature.toLongString());

        // 尝试从请求上下文（如果有的话）获得请求URL，以方便定位问题
        // RequestAttributes requestAttributes =
        // RequestContextHolder.getRequestAttributes();
        // if (requestAttributes != null) {
        // HttpServletRequest request = ((ServletRequestAttributes)
        // requestAttributes).getRequest();
        // if (request != null)
        // name += String.format("【%s】", request.getRequestURL().toString());
        // }
        // 实现的是入参的日志输出
        if (metrics.logParameters())
            log.info(String.format("【入参日志】调用 %s 的参数是：【%s】", name, this.serializer(joinPoint.getArgs())));
        // 实现连接点方法的执行，以及成功失败的打点，出现异常的时候还会记录日志
        Object returnValue;
        Instant start = Instant.now();
        try {
            returnValue = joinPoint.proceed();
            if (metrics.recordSuccessMetrics())
                // 在生产级代码中，我们应考虑使用类似Micrometer的指标框架，把打点信息记录到时间序列数据库中，实现通过图表来查看方法的调用次数和执行时间，在设计篇我们会重点介绍
                log.info(String.format("【成功打点】调用 %s 成功，耗时：%d ms", name,
                        Duration.between(start, Instant.now()).toMillis()));
        } catch (Exception ex) {
            if (metrics.recordFailMetrics()) {
                log.info(String.format("【失败打点】调用 %s 失败，耗时：%d ms", name,
                        Duration.between(start, Instant.now()).toMillis()));
            }
            if (metrics.logException()) {
                log.error(String.format("【异常日志】调用 %s 出现异常！", name), ex);
            }

            throw ex;
        }
        // 实现了返回值的日志输出
        if (metrics.logReturn()) {
            log.info(String.format("【出参日志】调用 %s 的返回是：【%s】", name, returnValue));
        }
        return returnValue;
    }

    private String serializer(Object... list) {
        if (list.length <= 0) {
            return "";
        }
        if (this.serializer == null) {
            log.warn("No Serializer");
            return "";
        }
        if (list.length == 1) {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                this.serializer.serialize(list[0], stream);
                return new String(stream.toByteArray());

            } catch (IOException e) {
                log.error("serializer failed", e);
                return "";
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (var object : list) {
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                this.serializer.serialize(object, stream);
                stringBuilder.append(new String(stream.toByteArray()));
                stream.reset();
            } catch (IOException e) {
                log.error("serializer failed", e);
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}