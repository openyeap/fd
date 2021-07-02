package ltd.fdsa.starter.limiter.core.factory;

import ltd.fdsa.starter.limiter.core.configure.TokenLimitedTrafficConfigure;
import ltd.fdsa.starter.limiter.core.recursion.RedisOperationsRecursion;

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
