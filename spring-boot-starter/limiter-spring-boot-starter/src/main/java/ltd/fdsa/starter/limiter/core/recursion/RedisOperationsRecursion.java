package ltd.fdsa.starter.limiter.core.recursion;

import lombok.Data;
import ltd.fdsa.starter.limiter.core.execution.RedisOperationsExecution;
import org.springframework.data.redis.core.BoundListOperations;

import java.util.concurrent.atomic.AtomicLong;


@Data
public class RedisOperationsRecursion {
    private AtomicLong offset;
    private Long maximumCapacity;

    public RedisOperationsRecursion(AtomicLong offset, Long maximumCapacity) {
        this.offset = offset;
        this.maximumCapacity = maximumCapacity;
    }

    public void addTokens(
            BoundListOperations<Object, Object> boundListOperations, Integer tokenValue, Long thisSize) {
        long thisOffset = offset.get();
        boolean loading = maximumCapacity > thisSize && thisOffset > 0;
        while (loading) {
            long update = thisOffset - 1;
            offset.compareAndSet(thisOffset, update);
            thisSize = RedisOperationsExecution.addTokens(boundListOperations, tokenValue);
            thisOffset = offset.get();
            loading = maximumCapacity > thisSize && thisOffset > 0;
        }
        offset.compareAndSet(thisOffset, 0);
    }
}
