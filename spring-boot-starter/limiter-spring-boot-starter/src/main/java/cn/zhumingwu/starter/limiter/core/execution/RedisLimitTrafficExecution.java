package cn.zhumingwu.starter.limiter.core.execution;

import cn.zhumingwu.starter.limiter.annotation.LimitTraffic;
import cn.zhumingwu.starter.limiter.core.recursion.RedisOperationsRecursion;
import cn.zhumingwu.starter.limiter.thread.AbstractAddTokenThread;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.starter.limiter.core.configure.TokenLimitedTrafficConfigure;
import cn.zhumingwu.starter.limiter.core.error.LimitException;
import cn.zhumingwu.starter.limiter.core.factory.RedisOperationsFactory;
import cn.zhumingwu.starter.limiter.core.factory.RedisOperationsRecursionFactory;
import cn.zhumingwu.starter.limiter.core.factory.TokenLimitedTrafficConfigureFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class RedisLimitTrafficExecution extends AbstractAddTokenThread {

    public RedisLimitTrafficExecution(
            RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic) {
        super(redisTemplate, method, limitTraffic);
    }

    @Override
    protected void startAddingTokens(
            RedisTemplate<Object, Object> redisTemplate, Method method, LimitTraffic limitTraffic) {
        TokenLimitedTrafficConfigure redisExecutionConfigure =
                TokenLimitedTrafficConfigureFactory.getRedisExecutionConfigure(method, limitTraffic);
        RedisOperationsRecursion redisOperationsRecursion =
                RedisOperationsRecursionFactory.getRedisOperationsRecursion(redisExecutionConfigure);
        BoundListOperations<Object, Object> boundListOperations =
                RedisOperationsFactory.getBoundListOperations(
                        redisTemplate, redisExecutionConfigure.getTokenKey());
        Long thisSize = boundListOperations.size();
        redisOperationsRecursion.addTokens(
                boundListOperations, redisExecutionConfigure.getTokenValue(), thisSize);
        BoundValueOperations<Object, Object> boundValueOperations =
                RedisOperationsFactory.getBoundValueOperations(
                        redisTemplate, redisExecutionConfigure.getLockKey());

        boolean condition = Boolean.TRUE;
        while (condition) {
            Boolean lock =
                    RedisOperationsExecution.lock(
                            boundValueOperations,
                            redisExecutionConfigure.getLockValue(),
                            redisExecutionConfigure.getIntervalTime(),
                            redisExecutionConfigure.getTimeUnit());
            if (lock) {
                long thisOffset =
                        redisOperationsRecursion.getOffset().get() + redisExecutionConfigure.getAddedQuantity();
                redisOperationsRecursion.setOffset(new AtomicLong(thisOffset));
                thisSize = boundListOperations.size();
                redisOperationsRecursion.addTokens(
                        boundListOperations, redisExecutionConfigure.getTokenValue(), thisSize);
                try {
                    Thread.sleep(
                            redisExecutionConfigure
                                    .getTimeUnit()
                                    .toMillis(redisExecutionConfigure.getIntervalTime()));
                } catch (InterruptedException e) {
                    condition = Boolean.FALSE;
                    log.error("Thread.sleep error", new LimitException(e));
                }
            }
        }
    }
}
