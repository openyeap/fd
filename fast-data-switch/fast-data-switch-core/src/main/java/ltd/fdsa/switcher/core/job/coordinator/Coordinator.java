package ltd.fdsa.switcher.core.job.coordinator;

import ltd.fdsa.switcher.core.job.model.HandleCallbackParam;
import ltd.fdsa.switcher.core.job.model.RegistryParam;
import ltd.fdsa.web.view.Result;

import java.util.List;
/**
 * 客户端调用服务端的接口定义
 */

public interface Coordinator {

    // ---------------------- callback ----------------------

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    public Result<String> callback(List<HandleCallbackParam> callbackParamList);

    // ---------------------- registry ----------------------

    /**
     * 客户端将自己的Handler 注册到服务中心
     *
     * @param registryParam
     * @return
     */
    public Result<String> registry(RegistryParam registryParam);

    /**
     * registry remove
     *
     * @param registryParam
     * @return
     */
    public Result<String> registryRemove(RegistryParam registryParam);
}
