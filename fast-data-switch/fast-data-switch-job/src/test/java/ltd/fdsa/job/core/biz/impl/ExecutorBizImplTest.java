package ltd.fdsa.job.core.biz.impl;

import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.invoker.route.LoadBalance;
import com.xxl.rpc.remoting.net.impl.netty_http.client.NettyHttpClient;
import com.xxl.rpc.serialize.impl.HessianSerializer;

import ltd.fdsa.job.core.biz.ExecutorBiz;
import ltd.fdsa.job.core.biz.model.LogResult;
import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.biz.model.TriggerParam;
import ltd.fdsa.job.core.enums.ExecutorBlockStrategyEnum;
import ltd.fdsa.job.core.executor.JobExecutor;
import ltd.fdsa.job.core.glue.GlueTypeEnum;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


public class ExecutorBizImplTest {

    public JobExecutor JobExecutor = null;
    public ExecutorBiz executorBiz = null;

    @Before
    public void before() throws Exception {

        // init executor
        JobExecutor = new JobExecutor();
        JobExecutor.setAdminAddresses(null);
        JobExecutor.setAppName("xxl-job-executor-sample");
        JobExecutor.setIp(null);
        JobExecutor.setPort(9999);
        JobExecutor.setAccessToken(null);
        JobExecutor.setLogPath("/data/applogs/xxl-job/jobhandler");
        JobExecutor.setLogRetentionDays(-1);

        // start executor
        JobExecutor.start();

        TimeUnit.SECONDS.sleep(3);

        // init executor biz proxy
        XxlRpcReferenceBean referenceBean = new XxlRpcReferenceBean();
        referenceBean.setClient(NettyHttpClient.class);
        referenceBean.setSerializer(HessianSerializer.class);
        referenceBean.setCallType(CallType.SYNC);
        referenceBean.setLoadBalance(LoadBalance.ROUND);
        referenceBean.setIface(ExecutorBiz.class);
        referenceBean.setVersion(null);
        referenceBean.setTimeout(3000);
        referenceBean.setAddress("127.0.0.1:9999");
        referenceBean.setAccessToken(null);
        referenceBean.setInvokeCallback(null);
        referenceBean.setInvokerFactory(null);

        executorBiz = (ExecutorBiz) referenceBean.getObject();
    }

    @After
    public void after(){
        if (JobExecutor != null) {
            JobExecutor.destroy();
        }
    }


    @Test
    public void beat() {
        // Act
        final ReturnT<String> retval = executorBiz.beat();

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((ReturnT<String>) retval).getContent());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMsg());
    }

    @Test
    public void idleBeat(){
        final int jobId = 0;

        // Act
        final ReturnT<String> retval = executorBiz.idleBeat(jobId);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((ReturnT<String>) retval).getContent());
        Assert.assertEquals(500, retval.getCode());
        Assert.assertEquals("job thread is running or has trigger queue.", retval.getMsg());
    }

    @Test
    public void kill(){
        final int jobId = 0;

        // Act
        final ReturnT<String> retval = executorBiz.kill(jobId);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((ReturnT<String>) retval).getContent());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMsg());
    }

    @Test
    public void log(){
        final long logDateTim = 0L;
        final long logId = 0;
        final int fromLineNum = 0;

        // Act
        final ReturnT<LogResult> retval = executorBiz.log(logDateTim, logId, fromLineNum);

        // Assert result
        Assert.assertNotNull(retval);
    }

    @Test
    public void run(){
        // trigger data
        final TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(1);
        triggerParam.setExecutorHandler("demoJobHandler");
        triggerParam.setExecutorParams(null);
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setGlueType(GlueTypeEnum.BEAN.name());
        triggerParam.setGlueSource(null);
        triggerParam.setGlueUpdatetime(System.currentTimeMillis());
        triggerParam.setLogId(1);
        triggerParam.setLogDateTime(System.currentTimeMillis());

        // Act
        final ReturnT<String> retval = executorBiz.run(triggerParam);

        // Assert result
        Assert.assertNotNull(retval);
    }

}
