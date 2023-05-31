package cn.zhumingwu.data.hub.admin.executor;

import cn.zhumingwu.base.model.Result;
import lombok.var;
import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.dataswitch.core.job.executor.JobExecutor;
import cn.zhumingwu.dataswitch.core.job.model.LogResult;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ExecutorBizImplTest {

    public JobExecutor JobExecutor = null;
    public Executor executorBiz = null;

    @Before
    public void before() throws Exception {

        // init executor
        JobExecutor = new JobExecutor(new Properties());

        // start executor
        JobExecutor.start();

        TimeUnit.SECONDS.sleep(3);
    }

    @After
    public void after() {
        if (JobExecutor != null) {
            JobExecutor.destroy();
        }
    }

    @Test
    public void beat() {
        // Act
        final var retval = executorBiz.stat();

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(retval.getData());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMessage());
    }

    @Test
    public void idleBeat() {
        final Long jobId = 0L;

        // Act
        final var retval = executorBiz.stat(jobId);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(retval.getData());
        Assert.assertEquals(500, retval.getCode());
        Assert.assertEquals("job thread is running or has trigger queue.", retval.getMessage());
    }

    @Test
    public void kill() {
        final Long jobId = 0L;

        // Act
        final var retval = executorBiz.stop(jobId, 0L);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull((retval).getData());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMessage());
    }

    @Test
    public void log() {
        final Long logId = 0L;
        // Act
        final Result<LogResult> retval = executorBiz.stat(logId, 0L);

        // Assert result
        Assert.assertNotNull(retval);
    }

    @Test
    public void run() {
        // trigger data
        final TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(1L);
        triggerParam.setHandler("demoJobHandler");
        triggerParam.setParams(null);
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategy.COVER);
        triggerParam.setTaskId(1L);
        triggerParam.setLogDateTime(System.currentTimeMillis());

        // Act
        var config = triggerParam.getParams();
        config.put("class", triggerParam.getHandler());
        config.put("strategy", triggerParam.getExecutorBlockStrategy().name());
        config.put("timeout", triggerParam.getTimeout() + "");

        final var retval = executorBiz.start(triggerParam.getJobId(), config);

        // Assert result
        Assert.assertNotNull(retval);
    }
}
