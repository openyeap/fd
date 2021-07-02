package ltd.fdsa.job.admin.scheduler;


import ltd.fdsa.job.admin.thread.JobFailMonitorHelper;
import ltd.fdsa.job.admin.thread.JobTriggerPoolHelper;
import ltd.fdsa.switcher.core.job.enums.ExecutorBlockStrategyEnum;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.switcher.core.job.executor.Executor;
import ltd.fdsa.switcher.core.job.executor.ExecutorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JobScheduler {
    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);
    // ---------------------- executor-client ----------------------
    private static ConcurrentMap<String, Executor> executorClientList = new ConcurrentHashMap<String, Executor>();

    public static Executor getExecutorClient(String address) throws Exception {
        // valid
        if (address == null || address.trim().length() == 0) {
            return null;
        }

        // load-cache
        address = address.trim();
        Executor executorClient = executorClientList.get(address);
        if (executorClient != null) {
            return executorClient;
        }
        executorClient = new ExecutorImpl();
        executorClientList.put(address, executorClient);
        return executorClient;
    }

    // ---------------------- I18n ----------------------

    public void init() throws Exception {
        // init i18n
        initI18n();

        // admin registry monitor run


        // admin monitor run
        JobFailMonitorHelper.getInstance().start();

        // admin trigger pool start
        JobTriggerPoolHelper.toStart();

        // admin log report start


        // start-schedule
        JobScheduleHelper.getInstance().start();
    }

    public void destroy() throws Exception {

        // stop-schedule
        JobScheduleHelper.getInstance().toStop();

        // admin log report stop


        // admin trigger pool stop
        JobTriggerPoolHelper.toStop();

        // admin monitor stop
        JobFailMonitorHelper.getInstance().toStop();

        // admin registry stop

    }

    private void initI18n() {
        for (ExecutorBlockStrategyEnum item : ExecutorBlockStrategyEnum.values()) {
            item.setTitle(I18nUtil.getString("jobconf_block_".concat(item.name())));
        }
    }
}
