package ltd.fdsa.starter.limiter.core.factory;

import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisOperationsFactory {
    private RedisOperationsFactory() {
    }

    public static BoundValueOperations<Object, Object> getBoundValueOperations(
            RedisTemplate<Object, Object> redisTemplate, String lockKey) {
        return redisTemplate.boundValueOps(lockKey);
    }

    public static BoundListOperations<Object, Object> getBoundListOperations(
            RedisTemplate<Object, Object> redisTemplate, byte[] tokenKey) {
        return redisTemplate.boundListOps(tokenKey);
    }
}
