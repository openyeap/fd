package ltd.fdsa.job.executor;

import ltd.fdsa.switcher.core.job.executor.Executor;
import ltd.fdsa.switcher.core.job.model.LogResult;
import ltd.fdsa.switcher.core.job.model.TriggerParam;
import ltd.fdsa.switcher.core.job.enums.ExecutorBlockStrategyEnum;
import ltd.fdsa.switcher.core.job.executor.JobExecutor;
import ltd.fdsa.web.view.Result;
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
        final Result<String> retval = executorBiz.beat();

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((Result<String>) retval).getData());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMessage());
    }

    @Test
    public void idleBeat() {
        final int jobId = 0;

        // Act
        final Result<String> retval = executorBiz.idleBeat(jobId);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((Result<String>) retval).getData());
        Assert.assertEquals(500, retval.getCode());
        Assert.assertEquals("job thread is running or has trigger queue.", retval.getMessage());
    }

    @Test
    public void kill() {
        final int jobId = 0;

        // Act
        final Result<String> retval = executorBiz.kill(jobId);

        // Assert result
        Assert.assertNotNull(retval);
        Assert.assertNull(((Result<String>) retval).getData());
        Assert.assertEquals(200, retval.getCode());
        Assert.assertNull(retval.getMessage());
    }

    @Test
    public void log() {
        final int logId = 0;
        // Act
        final Result<LogResult> retval = executorBiz.log(logId, "0");

        // Assert result
        Assert.assertNotNull(retval);
    }

    @Test
    public void run() {
        // trigger data
        final TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(1);
        triggerParam.setExecutorHandler("demoJobHandler");
        triggerParam.setExecutorParams(null);
        triggerParam.setExecutorBlockStrategy(ExecutorBlockStrategyEnum.COVER_EARLY.name());
        triggerParam.setLogId(1);
        triggerParam.setLogDateTime(System.currentTimeMillis());

        // Act
        final Result<String> retval = executorBiz.run(triggerParam.getJobId(), triggerParam.getExecutorHandler(), triggerParam.getExecutorBlockStrategy(), triggerParam.getExecutorTimeout(), triggerParam.getExecutorParams());

        // Assert result
        Assert.assertNotNull(retval);
    }
}
