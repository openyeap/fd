package ltd.fdsa.starter.limiter.thread;

import lombok.Data;
import ltd.fdsa.starter.limiter.annotation.LimitTraffic;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

@Data
public abstract class AbstractAddTokenThread implements Runnable {
    private RedisTemplate<Object, Object> redisTemplate;
    private Method method;
    private LimitTraffic limitTraffic;

    public AbstractAddTokenThread(
            RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic) {
        this.redisTemplate = redisTemplate;
        this.method = method;
        this.limitTraffic = limitTraffic;
    }

    /**
     * Start adding tokens
     *
     * @param redisTemplate redisTemplate
     * @param method        method
     * @param limitTraffic  limitTraffic
     */
    protected abstract void startAddingTokens(
            RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic);

    @Override
    public void run() {
        startAddingTokens(redisTemplate, method, limitTraffic);
    }
}
