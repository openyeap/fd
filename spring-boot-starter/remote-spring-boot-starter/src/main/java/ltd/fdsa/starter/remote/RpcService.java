package ltd.fdsa.starter.remote;

/*
 * 内部使用
 */
public interface RpcService {

    /*
     * 接收远程调用
     */
    <T> Object invoke(Class<T> requiredType, String methodName, Object... args);
}
