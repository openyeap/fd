package cn.zhumingwu.redis.lock;

import cn.zhumingwu.redis.properties.RedisConfigProperties;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.lock.LockManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.concurrent.locks.Lock;

/**
 * 基于Redis的互斥锁
 */
@Slf4j
public class RedisLockManager implements LockManager {

    private static RedisLockRegistry registry;
    private final RedisConfigProperties properties;
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisLockManager(RedisConfigProperties properties, RedisConnectionFactory redisConnectionFactory) {
        this.properties = properties;
        this.redisConnectionFactory = redisConnectionFactory;
    }


    @Override
    public Lock getLock(String lockKey) {
        if (registry == null) {
            synchronized (registry) {
                if (registry == null) {
                    registry = new RedisLockRegistry(redisConnectionFactory, this.properties.getCacheKey());
                }
            }
        }
        return registry.obtain(lockKey);
    }
}
