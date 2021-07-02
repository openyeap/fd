//package ltd.fdsa.job.controller;
//
//import ltd.fdsa.job.admin.annotation.PermissionLimit;
//import ltd.fdsa.job.core.biz.AdminBiz;
//import ltd.fdsa.job.core.model.HandleCallbackParam;
//import ltd.fdsa.job.core.model.RegistryParam;
//import ltd.fdsa.job.core.exception.JobException;
//import ltd.fdsa.job.util.JacksonUtil;
//import ltd.fdsa.web.view.Result;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@Controller
//@RequestMapping("/api")
//public class JobApiController {
//
//    @Resource
//    private AdminBiz adminBiz;
//
//    // ---------------------- base ----------------------
//
//    /**
//     * valid access token
//     */
//    private void validAccessToken(HttpServletRequest request) {
//
//    }
//
//    /**
//     * parse Param
//     */
//    private Object parseParam(String data, Class<?> parametrized, Class<?>... parameterClasses) {
//        Object param = null;
//        try {
//            if (parameterClasses != null) {
//                param = JacksonUtil.readValue(data, parametrized, parameterClasses);
//            } else {
//                param = JacksonUtil.readValue(data, parametrized);
//            }
//        } catch (Exception e) {
//        }
//        if (param == null) {
//            throw new JobException("The request data invalid.");
//        }
//        return param;
//    }
//
//    // ---------------------- admin biz ----------------------
//
//    /**
//     * callback
//     *
//     * @param data
//     * @return
//     */
//    @RequestMapping("/callback")
//    @ResponseBody
//    @PermissionLimit(limit = false)
//    public Result<String> callback(
//            HttpServletRequest request, @RequestBody(required = false) String data) {
//        // valid
//        validAccessToken(request);
//
//        // param
//        List<HandleCallbackParam> callbackParamList =
//                (List<HandleCallbackParam>) parseParam(data, List.class, HandleCallbackParam.class);
//
//        // invoke
//        return adminBiz.callback(callbackParamList);
//    }
//
//    /**
//     * registry
//     *
//     * @param data
//     * @return
//     */
//    @RequestMapping("/registry")
//    @ResponseBody
//    @PermissionLimit(limit = false)
//    public Result<String> registry(
//            HttpServletRequest request, @RequestBody(required = false) String data) {
//        // valid
//        validAccessToken(request);
//
//        // param
//        RegistryParam registryParam = (RegistryParam) parseParam(data, RegistryParam.class);
//
//        // invoke
//        return adminBiz.registry(registryParam);
//    }
//
//    /**
//     * registry remove
//     *
//     * @param data
//     * @return
//     */
//    @RequestMapping("/registryRemove")
//    @ResponseBody
//    @PermissionLimit(limit = false)
//    public Result<String> registryRemove(
//            HttpServletRequest request, @RequestBody(required = false) String data) {
//        // valid
//        validAccessToken(request);
//
//        // param
//        RegistryParam registryParam = (RegistryParam) parseParam(data, RegistryParam.class);
//
//        // invoke
//        return adminBiz.registryRemove(registryParam);
//    }
//
//    // ---------------------- job biz ----------------------
//
//}
