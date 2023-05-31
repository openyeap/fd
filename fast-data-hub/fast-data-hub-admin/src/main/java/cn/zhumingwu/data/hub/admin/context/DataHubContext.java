package cn.zhumingwu.data.hub.admin.context;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.event.ServiceDiscoveredEvent;
import cn.zhumingwu.base.service.InstanceInfo;
import cn.zhumingwu.data.hub.admin.entity.JobInfo;
import cn.zhumingwu.data.hub.admin.entity.JobTask;
import cn.zhumingwu.data.hub.admin.enums.ExecutorRouteStrategy;
import cn.zhumingwu.data.hub.admin.enums.TriggerType;
import cn.zhumingwu.data.hub.admin.service.impl.ExecutorClientImpl;
import cn.zhumingwu.data.hub.admin.thread.JobFailMonitor;
import cn.zhumingwu.data.hub.admin.thread.JobLogReporter;
import cn.zhumingwu.data.hub.admin.thread.JobScheduler;
import cn.zhumingwu.data.hub.admin.thread.JobTrigger;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobTaskRepository;
import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import com.caucho.hessian.client.HessianProxyFactory;
import com.google.common.base.Strings;
import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.context.ApplicationListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DataHubContext implements ApplicationListener<ServiceDiscoveredEvent>, SmartLifecycle {
    private static final Map<String, List<InstanceInfo>> CACHED_INSTANCE_RESULT = new HashMap<>();
    private static final AtomicBoolean CAN_CACHE_RESULT = new AtomicBoolean(false);
    private List<InstanceInfo> instanceList = new ArrayList<>();
    Lock lock = new ReentrantLock();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final JobScheduler jobScheduler;
    private final JobLogReporter jobLogReporter;
    private final JobTrigger jobTrigger;
    private final JobFailMonitor failMonitor;

    public CoordinatorContext() {
        this.jobScheduler = new JobScheduler(this);
        this.jobTrigger = new JobTrigger(this);
        this.failMonitor = new JobFailMonitor();
        this.jobLogReporter = new JobLogReporter();
    }

    @Override
    public void onApplicationEvent(ServiceDiscoveredEvent event) {

        List<InstanceInfo> list = new ArrayList<>();
        var services = event.getServices();
        for (var service : services.entrySet()) {
            list.addAll(service.getValue());
        }
        if (list.size() == 0) {
            return;
        }
        lock.lock();
        try {
            this.instanceList = list;
            CAN_CACHE_RESULT.set(false);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 通过计算表达式找到相应的实例
     *
     * @param handler    name
     * @param expression eg: cpu<10, service == 'jobs'
     * @return 满足条件的实例
     */
    public List<InstanceInfo> getInstanceInfoList(String handler, List<String> expression) {
        if (expression.size() == 0 && Strings.isNullOrEmpty(handler)) {
            return this.instanceList;
        }
        if (!CAN_CACHE_RESULT.get()) {
            CACHED_INSTANCE_RESULT.clear();
        }
        CAN_CACHE_RESULT.set(true);
        var key = handler + "," + String.join(",", expression);
        if (!CACHED_INSTANCE_RESULT.containsKey(key)) {
            lock.lock();
            if (!CACHED_INSTANCE_RESULT.containsKey(key)) {
                try {
                    if (expression.size() == 0) {
                        var list = instanceList.stream().filter(m -> {
                            return m.getMetadata().get("handlers").contains(handler + ",");
                        }).collect(Collectors.toList());
                        CACHED_INSTANCE_RESULT.put(key, list);
                    } else {
                        var exp = MessageFormat.format("return ({})", String.join(" && ", expression));
                        var list = this.instanceList.stream().filter(m -> {
                            var map = m.getMetadata();
                            if (map.get("handlers").contains(handler + ",")) {
                                return evaluate(new HashMap<String, Object>(map), exp);
                            }
                            return false;
                        }).collect(Collectors.toList());
                        CACHED_INSTANCE_RESULT.put(key, list);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
        return CACHED_INSTANCE_RESULT.get(key);
    }

    private Executor getExecutorClient(InstanceInfo[] instanceInfo) {
        List<Executor> list = new ArrayList<>(instanceInfo.length);
        HessianProxyFactory factory = new HessianProxyFactory();
        for (var i : instanceInfo) {
            Executor executor;
            try {
                executor = (Executor) factory.create(Executor.class, i.getUrl());
            } catch (MalformedURLException ex) {
                executor = null;
            }
            list.add(executor);
        }
        return new ExecutorClientImpl(list);
    }

    private boolean evaluate(Map<String, Object> map, String expression) {
        if (!expression.startsWith("return")) {
            expression = "return " + expression;
        }
        var exp = AviatorEvaluator.compile(expression);
        Object result = exp.execute(map);
        if (result == null) {
            return false;
        }
        return (Boolean) result;
    }


    /**
     * trigger job
     *
     * @param jobId              任务编号
     * @param triggerType        触发类型
     * @param retryTimes         >=0: use this param <0: use param from job info config
     * @param executorExpression 表达式
     * @param executorParam      null: use job param not null: cover job param
     */
    public void trigger(Long jobId, TriggerType triggerType, List<String> executorExpression, List<String> executorParam, int retryTimes) {
        // load data
        var optionalJobInfo = ApplicationContextHolder.getBean(JobInfoRepository.class).findById(jobId);
        if (!optionalJobInfo.isPresent()) {
            log.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
            return;
        }
        var jobInfo = optionalJobInfo.get();
        // block strategy
        if (executorParam != null && executorParam.size() > 0) {
            jobInfo.setExecutorParam(String.join(",", executorParam));
        }
        // route strategy
        if (executorExpression != null && executorExpression.size() > 0) {
            jobInfo.setExecutorExpression(String.join(",", executorExpression));
        }
        if (retryTimes > 0) {
            jobInfo.setExecutorRetryTimes(retryTimes);
        }
        trigger(jobInfo, triggerType);
    }


    /**
     * trigger job
     *
     * @param jobInfo     任凭信息
     * @param triggerType 触发类型
     */
    public void trigger(JobInfo jobInfo, TriggerType triggerType) {

        if (jobInfo == null) {
            log.warn("Trigger fail, jobInfo invalid");
            return;
        }

        // block strategy
        ExecutorBlockStrategy executorBlockStrategy = jobInfo.getExecutorBlockStrategy();
        var executorParam = Arrays.stream(jobInfo.getExecutorParam().split(",")).filter(m -> !Strings.isNullOrEmpty(m)).collect(Collectors.toList());

        // route strategy
        ExecutorRouteStrategy executorRouteStrategy = jobInfo.getExecutorRouteStrategy();
        var executorExpression = Arrays.stream(jobInfo.getScheduleExpression().split(",")).filter(m -> !Strings.isNullOrEmpty((m))).collect(Collectors.toList());
        // retry times
        var retryTimes = jobInfo.getExecutorRetryTimes();


        // 1、create new JobTask
        JobTask jobTask = new JobTask();
        jobTask.setJobId(jobInfo.getId());
        jobTask.setExecutorHandler(jobInfo.getExecutorHandler());
        jobTask.setExecutorParam(String.join(",", executorParam));
        jobTask.setExecutorRouterExpression(String.join(",", executorExpression));
        jobTask.setExecutorFailRetryCount(0);
        jobTask.setTriggerType(triggerType);
        jobTask.setTriggerMsg(triggerType.getTitle());
        jobTask.setTriggerTime(new Date());
        ApplicationContextHolder.getBean(JobTaskRepository.class).save(jobTask);

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setTaskId(jobTask.getId());
        triggerParam.setHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorBlockStrategy(executorBlockStrategy);
        triggerParam.setTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setRetryTimes(retryTimes);
        triggerParam.setParams(executorParam);
        triggerParam.setTimestamp(System.currentTimeMillis());
        // 3、init address
        var instanceInfos = executorRouteStrategy.getRouter().route(this, triggerParam, executorExpression);
        var cli = this.getExecutorClient(instanceInfos);
        cli.start(triggerParam);
        jobTask.setExecutorInstanceId(Arrays.stream(instanceInfos).map(m -> m.getIp() + m.getPort()).collect(Collectors.joining(",")));

        // 5、collection trigger info
        StringBuffer triggerMsgSb = new StringBuffer();
        // 6、save log trigger-info

        // jobLog.setTriggerTime();

        // ApplicationContextHolder.getBean(JobLogRepository.class).save(jobLog);

    }

    @Override
    public void start() {
        if (this.running.compareAndSet(false, true)) {
            // admin monitor run
            this.failMonitor.start();

            // admin trigger pool start
            this.jobTrigger.start();
            // admin log report start
            this.jobLogReporter.start();

            // start-schedule
            this.jobScheduler.start();
        }
    }

    @Override
    public void stop() {
        if (this.running.compareAndSet(true, false)) {
            // stop-schedule
            this.jobScheduler.stop();

            // admin log report stop
            this.jobLogReporter.stop();

            // admin trigger pool stop
            this.jobTrigger.stop();

            // admin monitor stop
            this.failMonitor.stop();

        }
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }
}
