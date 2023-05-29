package cn.zhumingwu.dataswitch.admin.route.strategy;

import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import lombok.var;


import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExecutorRouteRound implements ExecutorRouter {

    private static final ConcurrentMap<Long, Integer> routeCountEachJob = new ConcurrentHashMap<Long, Integer>();
    private static long CACHE_VALID_TIME = 0;

    @Override
    public InstanceInfo[] route(CoordinatorContext context, TriggerParam triggerParam, List<String> expression) {
        var list = context.getInstanceInfoList(triggerParam.getHandler(), expression);
        return new InstanceInfo[]{list.get(count(triggerParam.getJobId()) % list.size())};
    }

    private Integer count(Long jobId) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachJob.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }

        Integer count = routeCountEachJob.get(jobId);
        if (count == null) {
            count = new Random().nextInt() & 0B11111111;// 初始化时主动Random一次，缓解首次压力
        } else {
            count++;
        }
        return routeCountEachJob.put(jobId, count);
    }


}
