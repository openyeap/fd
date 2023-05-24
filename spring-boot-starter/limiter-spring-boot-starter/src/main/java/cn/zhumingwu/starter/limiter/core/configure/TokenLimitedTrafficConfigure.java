package cn.zhumingwu.starter.limiter.core.configure;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


@Data
public class TokenLimitedTrafficConfigure implements Serializable {
    private byte[] tokenKey;
    private Integer tokenValue;
    private String lockKey;
    private Integer lockValue;
    private long initialQuantity;
    private long maximumCapacity;
    private long addedQuantity;
    private long intervalTime;
    private TimeUnit timeUnit;
}
