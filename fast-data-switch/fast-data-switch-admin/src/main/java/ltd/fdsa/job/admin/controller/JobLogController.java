package ltd.fdsa.job.admin.controller;

import ltd.fdsa.job.admin.scheduler.JobScheduler;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;
import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import ltd.fdsa.job.admin.jpa.entity.JobLog;
import ltd.fdsa.job.admin.jpa.service.JobGroupService;
import ltd.fdsa.job.admin.jpa.service.JobInfoService;
import ltd.fdsa.job.admin.jpa.service.JobLogService;
import ltd.fdsa.switcher.core.exception.FastDataSwitchException;
import ltd.fdsa.switcher.core.job.executor.Executor;
import ltd.fdsa.switcher.core.job.model.LogResult;
import ltd.fdsa.job.admin.util.DateUtil;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * index controller
 */
@Controller
@RequestMapping("/joblog")
public class JobLogController {
    private static Logger logger = LoggerFactory.getLogger(JobLogController.class);
    @Resource
    public JobInfoService JobInfoDao;
    @Resource
    public JobLogService JobLogDao;
    @Resource
    private JobGroupService JobGroupDao;

    @RequestMapping
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(required = false, defaultValue = "0") Integer jobId) {

        // 执行器列表
        List<JobGroup> jobGroupList_all = JobGroupDao.findAll();

        // filter group
        List<JobGroup> jobGroupList = JobInfoController.filterJobGroupByRole(request, jobGroupList_all);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new FastDataSwitchException(I18nUtil.getString("jobgroup_empty"));
        }

        model.addAttribute("JobGroupList", jobGroupList);

        // 任务
        if (jobId > 0) {
            JobInfo jobInfo = JobInfoDao.findById(jobId).get();
            if (jobInfo == null) {
                throw new RuntimeException(
                        I18nUtil.getString("jobinfo_field_id") + I18nUtil.getString("system_invalid"));
            }

            model.addAttribute("jobInfo", jobInfo);
        }

        return "joblog/joblog.index";
    }

    @RequestMapping("/getJobsByGroup")
    @ResponseBody
    public Result<List<JobInfo>> getJobsByGroup(int jobGroup) {
        List<JobInfo> list = JobInfoDao.findAll().stream().filter(m -> m.getGroupId() == jobGroup).collect(Collectors.toList());
        return Result.success(list);
    }

    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int start, @RequestParam(required = false, defaultValue = "10") int length,
                                        int jobGroup,
                                        int jobId,
                                        int logStatus,
                                        String filterTime) {
        // parse param
        Date triggerTimeStart = null;
        Date triggerTimeEnd = null;
        if (filterTime != null && filterTime.trim().length() > 0) {
            String[] temp = filterTime.split(" - ");
            if (temp.length == 2) {
                triggerTimeStart = DateUtil.parseDateTime(temp[0]);
                triggerTimeEnd = DateUtil.parseDateTime(temp[1]);
            }
        }

//        // page query
//        List<JobLog> list =
//                JobLogDao.pageList(
//                        start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);
//        int list_count =
//                JobLogDao.pageListCount(
//                        start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
//        maps.put("recordsTotal", list_count); // 总记录数
//        maps.put("recordsFiltered", list_count); // 过滤后的总记录数
//        maps.put("data", list); // 分页列表
        return maps;
    }

    @RequestMapping("/logDetailPage")
    public String logDetailPage(int id, Model model) {

        // base check
        Result<String> logStatue = Result.success();
        JobLog jobLog = JobLogDao.findById(id).get();
        if (jobLog == null) {
            throw new RuntimeException(I18nUtil.getString("joblog_logid_invalid"));
        }

        model.addAttribute("triggerCode", jobLog.getTriggerCode());
        model.addAttribute("handleCode", jobLog.getHandleCode());
        model.addAttribute("executorAddress", jobLog.getExecutorAddress());
        model.addAttribute("triggerTime", jobLog.getTriggerTime().getTime());
        model.addAttribute("logId", jobLog.getId());
        return "joblog/joblog.detail";
    }

    @RequestMapping("/logDetailCat")
    @ResponseBody
    public Result<LogResult> logDetailCat(
            String executorAddress, long triggerTime, int logId, String fromLineNum) {
        try {
            Executor executorBiz = JobScheduler.getExecutorClient(executorAddress);
            Result<LogResult> logResult = executorBiz.log(logId, fromLineNum);

            // is end
            if (logResult.getData() != null
                    && logResult.getData().getFromLineNum() > logResult.getData().getToLineNum()) {
                JobLog jobLog = JobLogDao.findById(logId).get();
                if (jobLog.getHandleCode() > 0) {
                    logResult.getData().setEnd(true);
                }
            }

            return logResult;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fail(500, e.getMessage());
        }
    }

    @RequestMapping("/logKill")
    @ResponseBody
    public Result<String> logKill(int id) {
        // base check
        JobLog log = JobLogDao.findById(id).get();
        JobInfo jobInfo = JobInfoDao.findById(log.getJobId()).get();
        if (jobInfo == null) {
            return Result.fail(500, I18nUtil.getString("jobinfo_glue_jobid_invalid"));
        }
        if (Result.success().getCode() != log.getTriggerCode()) {
            return Result.fail(500, I18nUtil.getString("joblog_kill_log_limit"));
        }

        // request of kill
        Result<String> runResult = null;
        try {
            Executor executorBiz = JobScheduler.getExecutorClient(log.getExecutorAddress());
            runResult = executorBiz.kill(jobInfo.getId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            runResult = Result.fail(500, e.getMessage());
        }

        if (Result.success().getCode() == runResult.getCode()) {
            log.setHandleCode(HttpCode.INTERNAL_SERVER_ERROR.getCode());
            log.setHandleMsg(
                    I18nUtil.getString("joblog_kill_log_byman")
                            + ":"
                            + (runResult.getMessage() != null ? runResult.getMessage() : ""));
            log.setHandleTime(new Date());
            JobLogDao.update(log);
            return Result.success(runResult.getMessage());
        } else {
            return Result.fail(500, runResult.getMessage());
        }
    }

    @RequestMapping("/clearLog")
    @ResponseBody
    public Result<String> clearLog(int jobGroup, int jobId, int type) {

        Date clearBeforeTime = null;
        int clearBeforeNum = 0;
        if (type == 1) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -1); // 清理一个月之前日志数据
        } else if (type == 2) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -3); // 清理三个月之前日志数据
        } else if (type == 3) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -6); // 清理六个月之前日志数据
        } else if (type == 4) {
            clearBeforeTime = DateUtil.addYears(new Date(), -1); // 清理一年之前日志数据
        } else if (type == 5) {
            clearBeforeNum = 1000; // 清理一千条以前日志数据
        } else if (type == 6) {
            clearBeforeNum = 10000; // 清理一万条以前日志数据
        } else if (type == 7) {
            clearBeforeNum = 30000; // 清理三万条以前日志数据
        } else if (type == 8) {
            clearBeforeNum = 100000; // 清理十万条以前日志数据
        } else if (type == 9) {
            clearBeforeNum = 0; // 清理所有日志数据
        } else {
            return Result.fail(500, I18nUtil.getString("joblog_clean_type_invalid"));
        }

//        List<Long> logIds = null;
//        do {
//            logIds = JobLogDao.findClearLogIds(jobGroup, jobId, clearBeforeTime, clearBeforeNum, 1000);
//            if (logIds != null && logIds.size() > 0) {
//                JobLogDao.deleteAll(logIds.toArray(Integer[]::new));
//            }
//        } while (logIds != null && logIds.size() > 0);

        return Result.success();
    }
}
