package cn.zhumingwu.dataswitch.admin.route.strategy;

import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;



import java.util.List;

public class ExecutorRouteFirst implements ExecutorRouter {

    @Override
    public InstanceInfo[] route(CoordinatorContext context, TriggerParam triggerParam, List<String> expression) {
        var list = context.getInstanceInfoList(triggerParam.getHandler(), expression);
        return new InstanceInfo[]{
                list.get(0)
        };
    }
}
