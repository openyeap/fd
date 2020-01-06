package ltd.fdsa.job.core.biz;

import java.util.List;

import ltd.fdsa.job.core.biz.model.HandleCallbackParam;
import ltd.fdsa.job.core.biz.model.RegistryParam;
import ltd.fdsa.job.core.biz.model.ReturnT;


public interface AdminBiz {


    // ---------------------- callback ----------------------

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);


    // ---------------------- registry ----------------------

    /**
     * registry
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registry(RegistryParam registryParam);

    /**
     * registry remove
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registryRemove(RegistryParam registryParam);

}
