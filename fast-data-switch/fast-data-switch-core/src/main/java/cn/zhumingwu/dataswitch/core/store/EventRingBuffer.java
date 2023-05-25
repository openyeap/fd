package cn.zhumingwu.dataswitch.core.store;

import lombok.var;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 *
 * @author zhumingwu
 * @since 2022/1/30 13:23
 */
public class EventRingBuffer {
    private final EventMessage[] buffer;
    private final int size;
    private final int power;
    private final AtomicLong counter;
    private final ConcurrentSkipListSet<Long> cursor;

    public EventRingBuffer(long initialValue, int power) {
        if (power <= 0 || power > 32) {
            this.power = 10;
        } else {
            this.power = power;
        }
        this.size = 1 << power;
        this.counter = new AtomicLong(initialValue);
        this.buffer = new EventMessage[size];
        this.cursor = new ConcurrentSkipListSet<>();
    }

    public long getOffset(boolean increment) {
        if (increment) {
            return this.counter.incrementAndGet();
        }
        return this.counter.get();
    }

    public int getSize() {
        return this.size;
    }

    public EventMessage pull(long offset) {
        //如果事件不在缓存中
        if (offset < this.counter.get() - size) {
            return null;
        }

        EventMessage result = null;

        //如果事件还没发生
        int retries = 0;
        while (offset > this.counter.get()) {
            if (retries < 100) {
                //1、自旋N次
            } else if (retries < 200) {
                //2、屈服N次
                Thread.yield();
            } else {
                //3、休眠N次
                LockSupport.parkNanos(100);
            }
            retries++;
        }
        //锁定指针
        var needLock = offset < this.cursor.first();
        if (needLock) {
            this.cursor.add(offset);
        }
        if (offset > this.counter.get() - size && offset < this.counter.get()) {
            result = this.buffer[(int) offset & this.size];
        }
        if (needLock) {
            this.cursor.remove(offset);
        }
        return result;
    }

    public long push(EventMessage data) {
        //如果有消息费者使用
        int retries = 0;
        while (this.counter.get() + 1 >= this.cursor.first() + this.size) {
            if (retries < 100) {
                //1、自旋N次
            } else if (retries < 200) {
                //2、屈服N次
                Thread.yield();
            } else {
                //3、休眠N次
                LockSupport.parkNanos(100);
            }
            retries++;
        }
        var offset = this.counter.incrementAndGet();
        this.buffer[(int) offset & this.size] = data;
        return this.counter.get();
    }
}

