package ltd.fdsa.starter.limiter.core.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolFactory {
    private ThreadPoolFactory() {
    }

    public static ThreadPoolExecutor createTokenLimitedTrafficThreadPool(
            String beanName, Integer numberOfMethods) {
        ThreadFactory threadFactory =
                new ThreadFactoryBuilder().setNameFormat(beanName + "tokenLimitedTraffic").build();
        return new ThreadPoolExecutor(
                numberOfMethods,
                numberOfMethods,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }
}
