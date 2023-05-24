package cn.zhumingwu.starter.limiter.core.execution;

import lombok.Data;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;

import java.util.concurrent.TimeUnit;


@Data
public class RedisOperationsExecution {
    private RedisOperationsExecution() {
    }

    public static Boolean lock(
            BoundValueOperations<Object, Object> boundValueOperations,
            Integer lockValue,
            Long timeout,
            TimeUnit timeUnit) {
        return boundValueOperations.setIfAbsent(lockValue, timeout, timeUnit);
    }

    public static Long addTokens(
            BoundListOperations<Object, Object> boundListOperations, Integer tokenValue) {
        return boundListOperations.leftPush(tokenValue);
    }

    public static Object getToken(BoundListOperations<Object, Object> boundListOperations) {
        return boundListOperations.leftPop();
    }
}
