package ltd.fdsa.job.adminbiz;

import ltd.fdsa.job.core.biz.AdminBiz;
import ltd.fdsa.job.core.biz.model.RegistryParam;
import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.enums.RegistryConfig;
import com.xxl.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.invoker.route.LoadBalance;
 
import com.xxl.rpc.serialize.Serializer;
import org.junit.Assert;
import org.junit.Test;


public class AdminBizTest {

    // admin-client
    private static String addressUrl = "http://127.0.0.1:8080/-admin".concat(AdminBiz.MAPPING);
    private static String accessToken = null;

    /**
     * registry executor
     *
     * @throws Exception
     */
    @Test
    public void registryTest() throws Exception {
        addressUrl = addressUrl.replace("http://", "");
        AdminBiz adminBiz = (AdminBiz) new XxlRpcReferenceBean(
                NetEnum.NETTY_HTTP,
                Serializer.SerializeEnum.HESSIAN.getSerializer(),
                CallType.SYNC,
                LoadBalance.ROUND,
                AdminBiz.class,
                null,
                3000,
                addressUrl,
                accessToken,
                null,
                null).getObject();

        // test executor registry
        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), "-executor-example", "127.0.0.1:9999");
        ReturnT<String> returnT = adminBiz.registry(registryParam);
        Assert.assertTrue(returnT.getCode() == ReturnT.SUCCESS_CODE);

        // stop invoker
        XxlRpcInvokerFactory.getInstance().stop();
    }

    /**
     * registry executor remove
     *
     * @throws Exception
     */
    @Test
    public void registryRemove() throws Exception {
        addressUrl = addressUrl.replace("http://", "");
        AdminBiz adminBiz = (AdminBiz) new XxlRpcReferenceBean(
                NetEnum.NETTY_HTTP,
                Serializer.SerializeEnum.HESSIAN.getSerializer(),
                CallType.SYNC,
                LoadBalance.ROUND,
                AdminBiz.class,
                null,
                3000,
                addressUrl,
                accessToken,
                null,
                null).getObject();

        // test executor registry remove
        RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), "-executor-example", "127.0.0.1:9999");
        ReturnT<String> returnT = adminBiz.registryRemove(registryParam);
        Assert.assertTrue(returnT.getCode() == ReturnT.SUCCESS_CODE);

        // stop invoker
        XxlRpcInvokerFactory.getInstance().stop();
    }

}
