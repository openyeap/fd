package ltd.fdsa.job.admin.route.strategy;

import ltd.fdsa.job.admin.route.ExecutorRouter;
import ltd.fdsa.job.admin.scheduler.JobScheduler;
import ltd.fdsa.switcher.core.job.executor.Executor;
import ltd.fdsa.switcher.core.job.model.TriggerParam;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.web.view.Result;

import java.util.List;

public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public Result<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            Result<String> idleBeatResult = null;
            try {
                Executor executorBiz = JobScheduler.getExecutorClient(address);
                idleBeatResult = executorBiz.idleBeat(triggerParam.getJobId());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = Result.fail(500, "" + e);
            }
            idleBeatResultSB
                    .append((idleBeatResultSB.length() > 0) ? "<br><br>" : "")
                    .append(I18nUtil.getString("jobconf_idleBeat") + "：")
                    .append("<br>address：")
                    .append(address)
                    .append("<br>code：")
                    .append(idleBeatResult.getCode())
                    .append("<br>msg：")
                    .append(idleBeatResult.getMessage());

            // beat success
            if (idleBeatResult.getCode() == Result.success().getCode()) {
                idleBeatResult.setMessage(idleBeatResultSB.toString());
                idleBeatResult.setData(address);
                return idleBeatResult;
            }
        }

        return Result.fail(500, idleBeatResultSB.toString());
    }
}
