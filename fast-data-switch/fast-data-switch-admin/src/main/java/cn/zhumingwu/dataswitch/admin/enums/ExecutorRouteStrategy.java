package cn.zhumingwu.dataswitch.admin.enums;

import cn.zhumingwu.base.support.CommonEnum;
import cn.zhumingwu.dataswitch.admin.route.ExecutorRouter;
import cn.zhumingwu.dataswitch.admin.route.strategy.*;

public enum ExecutorRouteStrategy implements CommonEnum {
    ROUND(0, "route_round", new ExecutorRouteRound()),
    RANDOM(1, "route_random", new ExecutorRouteRandom()),
    CONSISTENT_HASH(2, "route_consistent_hash", new ExecutorRouteConsistentHash()),
    FIRST(3, "route_first", new ExecutorRouteFirst()),

    LAST(4, "route_last", new ExecutorRouteLast()),
    LEAST_FREQUENTLY_USED(6, "route_lfu", new ExecutorRouteLFU()),
    LEAST_RECENTLY_USED(7, "route_lru", new ExecutorRouteLRU()),
    SHARDING_BROADCAST(100, "route_shard", new ExecutorRouteShardingBroadcast());
    private final int code;
    private final String name;
    private final ExecutorRouter router;

    ExecutorRouteStrategy(int code, String name, ExecutorRouter router) {
        this.code = code;
        this.name = name;
        this.router = router;
    }

    public static ExecutorRouteStrategy match(String name, ExecutorRouteStrategy defaultItem) {
        if (name != null) {
            for (ExecutorRouteStrategy item : ExecutorRouteStrategy.values()) {
                if (item.name().equals(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.name;
    }

    public ExecutorRouter getRouter() {
        return router;
    }
}
