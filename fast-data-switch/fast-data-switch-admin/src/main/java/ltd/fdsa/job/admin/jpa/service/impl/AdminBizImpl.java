package ltd.fdsa.job.admin.jpa.service.impl;


import ltd.fdsa.job.admin.thread.JobTriggerPoolHelper;
import ltd.fdsa.job.admin.trigger.TriggerTypeEnum;
import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import ltd.fdsa.job.admin.jpa.entity.JobLog;
import ltd.fdsa.job.admin.jpa.service.JobGroupService;
import ltd.fdsa.job.admin.jpa.service.JobInfoService;
import ltd.fdsa.job.admin.jpa.service.JobLogService;
import ltd.fdsa.job.admin.jpa.service.JobRegistryService;
import ltd.fdsa.switcher.core.job.coordinator.Coordinator;
import ltd.fdsa.switcher.core.job.handler.JobHandler;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.switcher.core.job.model.HandleCallbackParam;
import ltd.fdsa.switcher.core.job.model.RegistryParam;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdminBizImpl implements Coordinator {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    public JobLogService JobLogDao;
    @Resource
    private JobInfoService JobInfoDao;
    @Resource
    private JobRegistryService JobRegistryDao;
    @Resource
    private JobGroupService JobGroupDao;

    @Override
    public Result<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam : callbackParamList) {
            Result<String> callbackResult = callback(handleCallbackParam);
            logger.debug(
                    ">>>>>>>>> JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode() == JobHandler.SUCCESS.getCode() ? "success" : "fail"),
                    handleCallbackParam,
                    callbackResult);
        }

        return Result.success();
    }

    private Result<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        JobLog log = JobLogDao.findById(handleCallbackParam.getLogId()).get();
        if (log == null) {
            return Result.fail(5000, "log item not found.");
        }
        if (log.getHandleCode() > 0) {
            return Result.fail(5000, "log repeate callback."); // avoid repeat callback, trigger child job etc
        }

        // trigger success, to trigger child job
        String callbackMsg = null;
        if (JobHandler.SUCCESS.getCode() == handleCallbackParam.getExecuteResult().getCode()) {
            JobInfo JobInfo = JobInfoDao.findById(log.getJobId()).get();
            if (JobInfo != null
                    && JobInfo.getChildJobId() != null
                    && JobInfo.getChildJobId().trim().length() > 0) {
                callbackMsg =
                        "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"
                                + I18nUtil.getString("jobconf_trigger_child_run")
                                + "<<<<<<<<<<< </span><br>";

                String[] childJobIds = JobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    int childJobId =
                            (childJobIds[i] != null
                                    && childJobIds[i].trim().length() > 0
                                    && isNumeric(childJobIds[i]))
                                    ? Integer.valueOf(childJobIds[i])
                                    : -1;
                    if (childJobId > 0) {

                        JobTriggerPoolHelper.trigger(childJobId, TriggerTypeEnum.PARENT, -1, null, null);
                        Result<String> triggerChildResult = Result.success();

                        // add msg
                        callbackMsg +=
                                MessageFormat.format(
                                        I18nUtil.getString("jobconf_callback_child_msg1"),
                                        (i + 1),
                                        childJobIds.length,
                                        childJobIds[i],
                                        (triggerChildResult.getCode() == HttpCode.OK.getCode()
                                                ? I18nUtil.getString("system_success")
                                                : I18nUtil.getString("system_fail")),
                                        triggerChildResult.getMessage());
                    } else {
                        callbackMsg +=
                                MessageFormat.format(
                                        I18nUtil.getString("jobconf_callback_child_msg2"),
                                        (i + 1),
                                        childJobIds.length,
                                        childJobIds[i]);
                    }
                }
            }
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg() != null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMessage() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMessage());
        }
        if (callbackMsg != null) {
            handleMsg.append(callbackMsg);
        }

        // success, save log
        log.setHandleTime(new Date());
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleMsg.toString());
        JobLogDao.update(log);

        return Result.success();
    }

    private boolean isNumeric(String str) {
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Result<String> registry(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
                || !StringUtils.hasText(registryParam.getRegistryKey())
                || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return Result.fail(5000, "Illegal Argument.");
        }

//        int ret =
//                JobRegistryDao.registryUpdate(
//                        registryParam.getRegistryGroup(),
//                        registryParam.getRegistryKey(),
//                        registryParam.getRegistryValue(),
//                        new Date());
//        if (ret < 1) {
//            JobRegistryDao.registrySave(
//                    registryParam.getRegistryGroup(),
//                    registryParam.getRegistryKey(),
//                    registryParam.getRegistryValue(),
//                    new Date());
//
//            // fresh
//            freshGroupRegistryInfo(registryParam);
//        }
        return Result.success();
    }

    @Override
    public Result<String> registryRemove(RegistryParam registryParam) {

        // valid
        if (!StringUtils.hasText(registryParam.getRegistryGroup())
                || !StringUtils.hasText(registryParam.getRegistryKey())
                || !StringUtils.hasText(registryParam.getRegistryValue())) {
            return Result.fail(5000, "Illegal Argument.");
        }

//        int ret =
//                JobRegistryDao.registryDelete(
//                        registryParam.getRegistryGroup(),
//                        registryParam.getRegistryKey(),
//                        registryParam.getRegistryValue());
//        if (ret > 0) {
//
//            // fresh
//            freshGroupRegistryInfo(registryParam);
//        }
        return Result.success();
    }

    private void freshGroupRegistryInfo(RegistryParam registryParam) {
        // Under consideration, prevent affecting core tables
    }
}
