package cn.zhumingwu.job.controller;


import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.coordinator.Coordinator;
import cn.zhumingwu.dataswitch.core.job.model.CallbackParam;
import cn.zhumingwu.starter.remote.annotation.RpcClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@Controller
@RequestMapping("/api")
public class JobApiController {

    @RpcClient
    private Coordinator coordinatorClient;

    // ---------------------- base ----------------------

    /**
     * valid access token
     */
    private void validAccessToken(HttpServletRequest request) {

    }

    // ---------------------- admin biz ----------------------

    /**
     * callback
     *
     * @param request
     * @param callbackParamList
     * @return
     */
    @RequestMapping("/callback")
    @ResponseBody

    public Result<String> callback(HttpServletRequest request, @RequestBody(required = false)   List<CallbackParam> callbackParamList) {
        // valid
        validAccessToken(request);

        // invoke
        return coordinatorClient.callback(callbackParamList);
    }


}
