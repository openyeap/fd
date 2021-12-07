package ltd.fdsa.web.exception;

/**
 * 异常通知器接口
 *
 */
public interface ExceptionAdvice {

    /**
     * 运行
     *
     * @param exception 异常对象
     */
    void run(RuntimeException exception);
}
