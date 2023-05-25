package cn.zhumingwu.dataswitch.core.job.coordinator;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.HandleCallbackParam;
import cn.zhumingwu.dataswitch.core.model.NewService;

import java.util.List;
import java.util.Map;

/**
 * 客户端调用服务端的接口定义
 */

public interface Coordinator {
    //
    public Result<String> createProcess(Map<String, String> props);
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
     * @param newService
     * @return
     */
    public Result<String> registry(NewService newService);

    /**
     * registry remove
     *
     * @param newService
     * @return
     */
    public Result<String> unRegistry(NewService newService);
}
