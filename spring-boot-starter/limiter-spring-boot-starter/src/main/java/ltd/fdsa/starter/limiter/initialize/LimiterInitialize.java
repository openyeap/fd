package ltd.fdsa.starter.limiter.initialize;

import lombok.Data;
import ltd.fdsa.starter.limiter.annotation.LimitTraffic;
import ltd.fdsa.starter.limiter.annotation.TokenLimitedTraffic;
import ltd.fdsa.starter.limiter.core.execution.RedisLimitTrafficExecution;
import ltd.fdsa.starter.limiter.core.execution.ThreadPoolExecutorExecution;
import ltd.fdsa.starter.limiter.core.factory.ThreadPoolFactory;
import ltd.fdsa.starter.limiter.core.utils.AnnotatedMethodsUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

// todo: zhumingwu read it
@Data
public class LimiterInitialize implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private RedisTemplate<Object, Object> redisTemplate;

    public LimiterInitialize(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void initialize() {
        Map<String, Object> beansWithAnnotation =
                applicationContext.getBeansWithAnnotation(TokenLimitedTraffic.class);
        beansWithAnnotation.forEach(
                (beanName, bean) -> {
                    Map<Method, LimitTraffic> methodAndAnnotation =
                            AnnotatedMethodsUtils.getMethodAndAnnotation(bean, LimitTraffic.class);
                    ThreadPoolExecutor tokenLimitedTrafficThreadPool =
                            ThreadPoolFactory.createTokenLimitedTrafficThreadPool(
                                    beanName, methodAndAnnotation.size());
                    methodAndAnnotation.forEach(
                            (method, limitTraffic) ->
                                    ThreadPoolExecutorExecution.statsThread(
                                            tokenLimitedTrafficThreadPool,
                                            new RedisLimitTrafficExecution(redisTemplate, method, limitTraffic)));
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
