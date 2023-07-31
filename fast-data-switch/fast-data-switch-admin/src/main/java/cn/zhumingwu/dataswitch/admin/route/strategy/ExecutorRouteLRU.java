package cn.zhumingwu.dataswitch.admin.route.strategy;

import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;



import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 单个JOB对应的每个执行器，最久未使用的优先被选举 a、LFU(Least Frequently Used)：最不经常使用，频率/次数 b(*)、LRU(Least Recently
 * Used)：最近最久未使用，时间
 */
public class ExecutorRouteLRU implements ExecutorRouter {

    private static ConcurrentMap<Integer, LinkedHashMap<InstanceInfo, InstanceInfo>> jobLRUMap = new ConcurrentHashMap<Integer, LinkedHashMap<InstanceInfo, InstanceInfo>>();
    private static long CACHE_VALID_TIME = 0;

    @Override
    public InstanceInfo[] route(CoordinatorContext context, TriggerParam triggerParam, List<String> expression) {
        var list = context.getInstanceInfoList(triggerParam.getHandler(), expression);
        return new InstanceInfo[]{route(triggerParam.getJobId().intValue(), list)};
    }

    private InstanceInfo route(int jobId, List<InstanceInfo> addressList) {

        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            jobLRUMap.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }

        // init lru
        LinkedHashMap<InstanceInfo, InstanceInfo> lruItem = jobLRUMap.get(jobId);
        if (lruItem == null) {
            /**
             * LinkedHashMap a、accessOrder：true=访问顺序排序（get/put时排序）；false=插入顺序排期；
             * b、removeEldestEntry：新增元素时将会调用，返回true时会删除最老元素；可封装LinkedHashMap并重写该方法，比如定义最大容量，超出是返回true即可实现固定长度的LRU算法；
             */
            lruItem = new LinkedHashMap<InstanceInfo, InstanceInfo>(16, 0.75f, true);
            jobLRUMap.putIfAbsent(jobId, lruItem);
        }

        // put new
        for (InstanceInfo address : addressList) {
            if (!lruItem.containsKey(address)) {
                lruItem.put(address, address);
            }
        }
        // remove old
        List<InstanceInfo> delKeys = new ArrayList<>();
        for (InstanceInfo existKey : lruItem.keySet()) {
            if (!addressList.contains(existKey)) {
                delKeys.add(existKey);
            }
        }
        if (delKeys.size() > 0) {
            for (InstanceInfo delKey : delKeys) {
                lruItem.remove(delKey);
            }
        }

        // load
        InstanceInfo eldestKey = lruItem.entrySet().iterator().next().getKey();
        InstanceInfo eldestValue = lruItem.get(eldestKey);
        return eldestValue;
    }


}
