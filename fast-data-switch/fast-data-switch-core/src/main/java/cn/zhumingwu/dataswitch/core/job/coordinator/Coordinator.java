package cn.zhumingwu.dataswitch.core.job.coordinator;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.CallbackParam;

import java.util.List;

/**
 * 客户端调用服务端的接口定义
 */

public interface Coordinator {

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    public Result<String> callback(List<CallbackParam> callbackParamList);


}
