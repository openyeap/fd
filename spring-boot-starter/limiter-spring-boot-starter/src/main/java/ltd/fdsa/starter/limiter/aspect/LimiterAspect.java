package ltd.fdsa.starter.limiter.aspect;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.starter.limiter.core.execution.RedisOperationsExecution;
import ltd.fdsa.starter.limiter.core.factory.RedisOperationsFactory;
import ltd.fdsa.starter.limiter.core.utils.AspectUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;


@Aspect
@Slf4j
@Data
public class LimiterAspect {
    private RedisTemplate<Object, Object> redisTemplate;

    public LimiterAspect(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut("@annotation(com.github.thierrysquirrel.annotation.LimitTraffic)")
    public void limitTrafficPointcut() {
        log.debug("Start limiting traffic");
    }

    @Around("limitTrafficPointcut()")
    public Object limitTrafficAround(ProceedingJoinPoint point) throws Throwable {
        Method method = AspectUtils.getMethod(point);
        BoundListOperations<Object, Object> boundListOperations =
                RedisOperationsFactory.getBoundListOperations(
                        redisTemplate, DigestUtils.sha1(method.toString()));
        Object token = RedisOperationsExecution.getToken(boundListOperations);
        if (ObjectUtils.isEmpty(token)) {
            return null;
        }
        return point.proceed();
    }
}
