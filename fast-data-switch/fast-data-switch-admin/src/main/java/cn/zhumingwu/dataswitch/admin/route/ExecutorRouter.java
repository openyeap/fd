package cn.zhumingwu.dataswitch.admin.route;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface ExecutorRouter {

    /**
     * route to executors
     *
     * @param context      上下文对象
     * @param triggerParam triggerParam
     * @param expression   JobHandler过滤条件
     * @return Executor Instance List
     */
    InstanceInfo[] route(CoordinatorContext context, TriggerParam triggerParam, List<String> expression);
}
