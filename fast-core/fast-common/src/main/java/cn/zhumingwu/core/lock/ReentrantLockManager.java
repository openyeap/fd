package cn.zhumingwu.core.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockManager implements LockManager {

    @Override
    public Lock getLock(String lockKey) {
        return new ReentrantLock();
    }
}
