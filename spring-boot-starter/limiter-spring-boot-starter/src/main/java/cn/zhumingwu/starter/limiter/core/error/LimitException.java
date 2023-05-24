package cn.zhumingwu.starter.limiter.core.error;


public class LimitException extends Exception {
    public LimitException(String message) {
        super(message);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
