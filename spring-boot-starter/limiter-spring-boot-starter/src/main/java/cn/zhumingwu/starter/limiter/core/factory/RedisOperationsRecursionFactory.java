package cn.zhumingwu.starter.limiter.core.factory;

import cn.zhumingwu.starter.limiter.core.configure.TokenLimitedTrafficConfigure;
import cn.zhumingwu.starter.limiter.core.recursion.RedisOperationsRecursion;

import java.util.concurrent.atomic.AtomicLong;

public class RedisOperationsRecursionFactory {
    private RedisOperationsRecursionFactory() {
    }

    public static RedisOperationsRecursion getRedisOperationsRecursion(
            TokenLimitedTrafficConfigure tokenLimitedTrafficConfigure) {
        return new RedisOperationsRecursion(
                new AtomicLong(tokenLimitedTrafficConfigure.getInitialQuantity()),
                tokenLimitedTrafficConfigure.getMaximumCapacity());
    }
}
