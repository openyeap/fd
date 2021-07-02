package ltd.fdsa.starter.limiter.core.execution;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolExecutorExecution {
    private ThreadPoolExecutorExecution() {
    }

    public static void statsThread(ThreadPoolExecutor threadPoolExecutor, Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }
}
