package ltd.fdsa.consul.lock;

import com.ecwid.consul.v1.ConsulClient;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.lock.LockManager;

import java.util.concurrent.locks.Lock;

/**
 * 基于Consul的互斥锁
 */
@Slf4j
public class ConsulLockManager implements LockManager {
    private final ConsulClient consulClient;

    public ConsulLockManager(ConsulClient consulClient) {
        this.consulClient = consulClient;
    }

    @Override
    public Lock getLock(String lockKey) {
        return new ConsulLock(consulClient, lockKey);
    }
}
