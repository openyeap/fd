package cn.zhumingwu.dataswitch.admin.route.strategy;

import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;


import java.util.List;
import java.util.Random;

public class ExecutorRouteRandom extends ExecutorRouter {

    private static Random localRandom = new Random();

    @Override
    public Result<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return Result.success(address);
    }
}
