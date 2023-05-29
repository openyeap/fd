package cn.zhumingwu.dataswitch.admin.route.strategy;

import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import lombok.var;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 单个JOB对应的每个执行器，使用频率最低的优先被选举 a(*)、LFU(Least Frequently Used)：最不经常使用，频率/次数 b、LRU(Least Recently
 * Used)：最近最久未使用，时间
 */
public class ExecutorRouteLFU implements ExecutorRouter {

    private static ConcurrentMap<Integer, HashMap<InstanceInfo, Integer>> jobLfuMap = new ConcurrentHashMap<Integer, HashMap<InstanceInfo, Integer>>();
    private static long CACHE_VALID_TIME = 0;

    @Override
    public InstanceInfo[] route(CoordinatorContext context, TriggerParam triggerParam, List<String> expression) {
        var list = context.getInstanceInfoList(triggerParam.getHandler(), expression);
        return new InstanceInfo[]{route(triggerParam.getJobId().intValue(), list)};
    }

    private InstanceInfo route(int jobId, List<InstanceInfo> list) {

        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            jobLfuMap.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }

        // lfu item init
        HashMap<InstanceInfo, Integer> lfuItemMap =
                jobLfuMap.get(jobId); // Key排序可以用TreeMap+构造入参Compare；Value排序暂时只能通过ArrayList；
        if (lfuItemMap == null) {
            lfuItemMap = new HashMap<InstanceInfo, Integer>();
            jobLfuMap.putIfAbsent(jobId, lfuItemMap); // 避免重复覆盖
        }

        // put new
        for (var address : list) {
            if (!lfuItemMap.containsKey(address) || lfuItemMap.get(address) > 1000000) {
                lfuItemMap.put(address, new Random().nextInt(list.size())); // 初始化时主动Random一次，缓解首次压力
            }
        }
        // remove old
        List<InstanceInfo> delKeys = new ArrayList<>();
        for (InstanceInfo existKey : lfuItemMap.keySet()) {
            if (!list.contains(existKey)) {
                delKeys.add(existKey);
            }
        }
        if (delKeys.size() > 0) {
            for (InstanceInfo delKey : delKeys) {
                lfuItemMap.remove(delKey);
            }
        }

        // load least userd count address
        List<Map.Entry<InstanceInfo, Integer>> lfuItemList = new ArrayList<Map.Entry<InstanceInfo, Integer>>(lfuItemMap.entrySet());
        Collections.sort(
                lfuItemList,
                new Comparator<Map.Entry<InstanceInfo, Integer>>() {
                    @Override
                    public int compare(Map.Entry<InstanceInfo, Integer> o1, Map.Entry<InstanceInfo, Integer> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                });

        Map.Entry<InstanceInfo, Integer> addressItem = lfuItemList.get(0);
        InstanceInfo minAddress = addressItem.getKey();
        addressItem.setValue(addressItem.getValue() + 1);

        return addressItem.getKey();
    }
}
