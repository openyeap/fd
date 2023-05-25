package cn.zhumingwu.dataswitch.admin.trigger;

import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.dataswitch.admin.enums.TriggerTypeEnum;
import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobLogRepository;
import cn.zhumingwu.dataswitch.admin.scheduler.JobScheduler;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.dataswitch.core.job.model.TriggerParam;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import cn.zhumingwu.dataswitch.admin.entity.JobLog;
import cn.zhumingwu.dataswitch.admin.enums.ExecutorRouteStrategy;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;

@Slf4j
public class JobTrigger {

    /**
     * trigger job
     *
     * @param jobId
     * @param triggerType
     * @param failRetryCount        >=0: use this param <0: use param from job info config
     * @param executorShardingParam
     * @param executorParam         null: use job param not null: cover job param
     */
    public static void trigger(Long jobId, TriggerTypeEnum triggerType, int failRetryCount, String executorShardingParam, String executorParam) throws UnknownHostException {
        // load data
        JobInfo jobInfo = ApplicationContextHolder.getBean(JobInfoRepository.class).findById(jobId.intValue()).get();
        if (jobInfo == null) {
            log.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
            return;
        }
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }
        int finalFailRetryCount =
                failRetryCount >= 0 ? failRetryCount : jobInfo.getExecutorFailRetryCount();
        JobGroup group = ApplicationContextHolder.getBean(JobGroupRepository.class).findById(jobInfo.getGroupId().intValue()).get();

        // sharding param
        int[] shardingParam = null;
        if (executorShardingParam != null) {
            String[] shardingArr = executorShardingParam.split("/");
            if (shardingArr.length == 2 && isNumeric(shardingArr[0]) && isNumeric(shardingArr[1])) {
                shardingParam = new int[2];
                shardingParam[0] = Integer.valueOf(shardingArr[0]);
                shardingParam[1] = Integer.valueOf(shardingArr[1]);
            }
        }

        if (shardingParam == null) {
            shardingParam = new int[]{0, 1};
        }
        processTrigger(group, jobInfo, finalFailRetryCount, triggerType, shardingParam[0], shardingParam[1]);

    }

    private static boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @param group               job group, registry list may be empty
     * @param jobInfo
     * @param finalFailRetryCount
     * @param triggerType
     * @param index               sharding index
     * @param total               sharding index
     */
    private static void processTrigger(JobGroup group, JobInfo jobInfo, int finalFailRetryCount, TriggerTypeEnum triggerType, int index, int total) throws UnknownHostException {

        // param
        // block strategy
        ExecutorBlockStrategy blockStrategy = jobInfo.getExecutorBlockStrategy();
        // route strategy
        ExecutorRouteStrategy executorRouteStrategyEnum = jobInfo.getExecutorRouteStrategy();
        String shardingParam = (ExecutorRouteStrategy.SHARDING_BROADCAST == executorRouteStrategyEnum)
                ? String.valueOf(index).concat("/").concat(String.valueOf(total))
                : null;

        // 1、save log-id
        JobLog jobLog = new JobLog();
        jobLog.setJobGroup(jobInfo.getGroupId().intValue());
        jobLog.setJobId(jobInfo.getId().intValue());
        jobLog.setTriggerTime(new Date());
        ApplicationContextHolder.getBean(JobLogRepository.class).save(jobLog);

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setHandler(jobInfo.getExecutorHandler());
//todo        triggerParam.setExecutorParams( jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setTaskId(jobLog.getId().longValue());
        triggerParam.setLogDateTime(jobLog.getTriggerTime().getTime());
        triggerParam.setBroadcastIndex(index);
        triggerParam.setBroadcastTotal(total);

        // 3、init address
        String address = null;
        Result<String> routeAddressResult = null;
//        if (group.getRegistryList() != null && !group.getRegistryList().isEmpty()) {
//            if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum) {
//                if (index < group.getRegistryList().size()) {
//                    address = group.getRegistryList().get(index);
//                } else {
//                    address = group.getRegistryList().get(0);
//                }
//            } else {
//                routeAddressResult =
//                        executorRouteStrategyEnum.getRouter().route(triggerParam, group.getRegistryList());
//                if (routeAddressResult.getCode() == Result.success().getCode()) {
//                    address = routeAddressResult.getData();
//                }
//            }
//        } else {
        routeAddressResult = Result.fail(500, I18nUtil.getInstance("").getString("jobconf_trigger_address_empty"));
//        }

        // 4、trigger remote executor
        Result<Long> triggerResult = null;
        if (address != null) {
            triggerResult = runExecutor(triggerParam, address);
        } else {
            triggerResult = Result.fail(500, null);
        }

        // 5、collection trigger info
        StringBuffer triggerMsgSb = new StringBuffer();
        triggerMsgSb
                .append(I18nUtil.getInstance("").getString("jobconf_trigger_type"))
                .append("：")
                .append(triggerType.getTitle());
        triggerMsgSb
                .append("<br>")
                .append(I18nUtil.getInstance("").getString("jobconf_trigger_admin_adress"))
                .append("：")
                .append(Inet4Address.getLocalHost().getHostAddress());
        triggerMsgSb
                .append("<br>")
                .append(I18nUtil.getInstance("").getString("jobconf_trigger_exe_regtype"))
                .append("：")
                .append(
                        (group.getType() == 0)
                                ? I18nUtil.getInstance("").getString("jobgroup_field_addressType_0")
                                : I18nUtil.getInstance("").getString("jobgroup_field_addressType_1"));
        triggerMsgSb
                .append("<br>")
                .append(I18nUtil.getInstance("").getString("jobconf_trigger_exe_regaddress"))
                .append("：")
                .append("");
        triggerMsgSb
                .append("<br>")
                .append(I18nUtil.getInstance("").getString("jobinfo_field_executorRouteStrategy"))
                .append("：")
                .append(executorRouteStrategyEnum.getTitle());
        if (shardingParam != null) {
            triggerMsgSb.append("(" + shardingParam + ")");
        }
        triggerMsgSb
                .append("<br>")
                .append(I18nUtil.getInstance("").getString("jobinfo_field_executorBlockStrategy"))
                .append("：")
                .append(blockStrategy.getName());
        triggerMsgSb
                .append("<br>")
                .append(I18nUtil.getInstance("").getString("jobinfo_field_timeout"))
                .append("：")
                .append(jobInfo.getExecutorTimeout());
        triggerMsgSb
                .append("<br>")
                .append(I18nUtil.getInstance("").getString("jobinfo_field_executorFailRetryCount"))
                .append("：")
                .append(finalFailRetryCount);

        triggerMsgSb
                .append(
                        "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"
                                + I18nUtil.getInstance("").getString("jobconf_trigger_run")
                                + "<<<<<<<<<<< </span><br>")
                .append(
                        (routeAddressResult != null && routeAddressResult.getMessage() != null)
                                ? routeAddressResult.getMessage() + "<br><br>"
                                : "")
                .append(triggerResult.getMessage() != null ? triggerResult.getMessage() : "");

        // 6、save log trigger-info
        jobLog.setExecutorAddress(address);
        jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        jobLog.setExecutorParam(jobInfo.getExecutorParam());
        jobLog.setExecutorShardingParam(shardingParam);
        jobLog.setExecutorFailRetryCount(finalFailRetryCount);
        // jobLog.setTriggerTime();
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerMsgSb.toString());
        ApplicationContextHolder.getBean(JobLogRepository.class).save(jobLog);
    }

    /**
     * run executor
     *
     * @param triggerParam
     * @param address
     * @return
     */
    public static Result<Long> runExecutor(TriggerParam triggerParam, String address) {
        Result<Long> runResult = null;
        try {
            Executor executorBiz = JobScheduler.getExecutorClient(address);
            var config = triggerParam.getParams();
            config.put("class", triggerParam.getHandler());
            config.put("strategy", triggerParam.getExecutorBlockStrategy().name());
            config.put("timeout", triggerParam.getTimeout() + "");
            runResult = executorBiz.start(triggerParam.getJobId(), config);
        } catch (Exception ex) {
            runResult = Result.error(ex);
        }

        StringBuffer runResultSB = new StringBuffer(I18nUtil.getInstance("").getString("jobconf_trigger_run") + "：");
        runResultSB.append("<br>address：").append(address);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMessage());

        runResult.setMessage(runResultSB.toString());
        return runResult;
    }
}
