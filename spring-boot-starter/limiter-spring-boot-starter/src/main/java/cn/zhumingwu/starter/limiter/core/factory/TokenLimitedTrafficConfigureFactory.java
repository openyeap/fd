package cn.zhumingwu.starter.limiter.core.factory;

import cn.zhumingwu.starter.limiter.annotation.LimitTraffic;
import cn.zhumingwu.starter.limiter.core.configure.TokenLimitedTrafficConfigure;
import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;


public class TokenLimitedTrafficConfigureFactory {
    private TokenLimitedTrafficConfigureFactory() {
    }

    public static TokenLimitedTrafficConfigure getRedisExecutionConfigure(
            Method method, LimitTraffic limitTraffic) {
        TokenLimitedTrafficConfigure redisExecutionConfigure = new TokenLimitedTrafficConfigure();
        String methodString = method.toString();
        byte[] tokenKey = DigestUtils.sha1(methodString);
        redisExecutionConfigure.setTokenKey(tokenKey);
        redisExecutionConfigure.setTokenValue(Arrays.hashCode(tokenKey));

        String lockKey = DigestUtils.sha1Hex(methodString);
        redisExecutionConfigure.setLockKey(lockKey);
        redisExecutionConfigure.setLockValue(lockKey.hashCode());

        redisExecutionConfigure.setInitialQuantity(limitTraffic.initialQuantity());
        redisExecutionConfigure.setMaximumCapacity(limitTraffic.maximumCapacity());
        redisExecutionConfigure.setAddedQuantity(limitTraffic.addedQuantity());
        redisExecutionConfigure.setIntervalTime(limitTraffic.intervalTime());
        redisExecutionConfigure.setTimeUnit(limitTraffic.timeUnit());

        return redisExecutionConfigure;
    }
}
