package ltd.fdsa.job.admin.core.route.strategy;

import ltd.fdsa.job.core.biz.model.ReturnT;
import ltd.fdsa.job.core.biz.model.TriggerParam;

import ltd.fdsa.job.admin.core.route.ExecutorRouter;

import java.util.List;
import java.util.Random;


public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return new ReturnT<String>(address);
    }

}
