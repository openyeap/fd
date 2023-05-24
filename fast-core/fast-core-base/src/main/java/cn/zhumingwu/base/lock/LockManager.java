package cn.zhumingwu.base.lock;

import java.util.concurrent.locks.Lock;


public interface LockManager {
    Lock getLock(String lockKey);
}
