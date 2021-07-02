package ltd.fdsa.job.admin.route.strategy;

import ltd.fdsa.job.admin.route.ExecutorRouter;
import ltd.fdsa.switcher.core.job.model.TriggerParam;
import ltd.fdsa.web.view.Result;

import java.util.List;

public class ExecutorRouteFirst extends ExecutorRouter {

    @Override
    public Result<String> route(TriggerParam triggerParam, List<String> addressList) {
        return Result.success(addressList.get(0));
    }
}
