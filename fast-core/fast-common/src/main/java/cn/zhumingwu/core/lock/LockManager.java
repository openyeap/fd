package cn.zhumingwu.core.lock;

import java.util.concurrent.locks.Lock;


public interface LockManager {
    Lock getLock(String lockKey);
}
