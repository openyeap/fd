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
     * @param callbackParamList 任务的及时状态
     * @return 成功或失败
     */
    public Result<String> callback(List<CallbackParam> callbackParamList);


}
