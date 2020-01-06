package ltd.fdsa.job.admin.core.route.strategy;

import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.biz.model.TriggerParam;

import ltd.fdsa.job.admin.core.route.ExecutorRouter;

import java.util.List;


public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        return new ReturnT<String>(addressList.get(addressList.size()-1));
    }

}
