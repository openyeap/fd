package cn.zhumingwu.dataswitch.admin.route.strategy;

import cn.zhumingwu.dataswitch.admin.scheduler.JobScheduler;
import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;


import java.util.List;
import java.util.Map;

public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public Result<String> route(TriggerParam triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            Result<Map<String, String>> idleBeatResult = null;
            try {
                Executor executorBiz = JobScheduler.getExecutorClient(address);
                idleBeatResult = executorBiz.stat(triggerParam.getJobId().longValue());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = Result.fail(500, "" + e);
            }
            idleBeatResultSB
                    .append((idleBeatResultSB.length() > 0) ? "<br><br>" : "")
                    .append(I18nUtil.getInstance("").getString("jobconf_idleBeat") + "：")
                    .append("<br>address：")
                    .append(address)
                    .append("<br>code：")
                    .append(idleBeatResult.getCode())
                    .append("<br>msg：")
                    .append(idleBeatResult.getMessage());

            // beat success
            if (idleBeatResult.getCode() == Result.OK) {
                idleBeatResult.setMessage(idleBeatResultSB.toString());

                return Result.success(address);
            }
        }

        return Result.fail(500, idleBeatResultSB.toString());
    }
}
