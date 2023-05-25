package cn.zhumingwu.dataswitch.admin.controller;

import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobLogRepository;
import cn.zhumingwu.dataswitch.admin.entity.User;
import cn.zhumingwu.dataswitch.admin.scheduler.JobScheduler;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import cn.zhumingwu.dataswitch.admin.entity.JobLog;

import cn.zhumingwu.dataswitch.core.exception.FastDataSwitchException;
import cn.zhumingwu.dataswitch.core.job.executor.Executor;
import cn.zhumingwu.dataswitch.core.job.model.LogResult;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.DateUtil;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;

import cn.zhumingwu.dataswitch.admin.service.impl.SystemUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * index controller
 */
@Controller
@RequestMapping("/joblog")
public class JobLogController {
    private static Logger logger = LoggerFactory.getLogger(JobLogController.class);
    @Resource
    private JobGroupRepository jobGroupService;

    @Resource
    public JobInfoRepository JobInfoDao;
    @Resource
    public JobLogRepository jobLogRepository;

    @Resource
    private SystemUserService userService;

    List<JobGroup> filterJobGroupByRole(List<JobGroup> jobGroupList_all) {
        List<JobGroup> jobGroupList = new ArrayList<>();
        if (jobGroupList_all != null && jobGroupList_all.size() > 0) {
            User loginUser = this.userService.checkLogin();
            if (loginUser.getType() == 1) {
                jobGroupList = jobGroupList_all;
            } else {
                List<String> groupIdStrs = new ArrayList<>();

                for (JobGroup groupItem : jobGroupList_all) {
                    if (groupIdStrs.contains(String.valueOf(groupItem.getId()))) {
                        jobGroupList.add(groupItem);
                    }
                }
            }
        }
        return jobGroupList;
    }

    @RequestMapping
    public String index(Model model, @RequestParam(required = false, defaultValue = "0") Integer jobId) {

        // 执行器列表
        List<JobGroup> jobGroupList_all = this.jobGroupService.findAll();

        // filter group
        List<JobGroup> jobGroupList = filterJobGroupByRole(jobGroupList_all);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new FastDataSwitchException(I18nUtil.getInstance("").getString("jobgroup_empty"));
        }

        model.addAttribute("JobGroupList", jobGroupList);

        // 任务
        if (jobId > 0) {
            JobInfo jobInfo = JobInfoDao.findById(jobId).get();
            if (jobInfo == null) {
                throw new RuntimeException(
                        I18nUtil.getInstance("").getString("jobinfo_field_id") + I18nUtil.getInstance("").getString("system_invalid"));
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
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start, @RequestParam(required = false, defaultValue = "10") int length,
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
        JobLog jobLog = jobLogRepository.findById(id).get();
        if (jobLog == null) {
            throw new RuntimeException(I18nUtil.getInstance("").getString("joblog_logid_invalid"));
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
            String executorAddress, long triggerTime, Long logId, Long fromLineNum) {
        try {
            Executor executorBiz = JobScheduler.getExecutorClient(executorAddress);
            Result<LogResult> logResult = executorBiz.stat(logId, fromLineNum);

            // is end
            if (logResult.getData() != null
                    && logResult.getData().getFromLineNum() > logResult.getData().getToLineNum()) {
                JobLog jobLog = jobLogRepository.findById(logId.intValue()).get();
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
        JobLog log = jobLogRepository.findById(id).get();
        JobInfo jobInfo = JobInfoDao.findById(log.getJobId()).get();
        if (jobInfo == null) {
            return Result.fail(500, I18nUtil.getInstance("").getString("jobinfo_glue_jobid_invalid"));
        }
        if (Result.success().getCode() != log.getTriggerCode()) {
            return Result.fail(500, I18nUtil.getInstance("").getString("joblog_kill_log_limit"));
        }

        // request of kill
        Result<String> runResult = null;
        try {
            Executor executorBiz = JobScheduler.getExecutorClient(log.getExecutorAddress());
            runResult = executorBiz.stop(  jobInfo.getId() ,0L);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            runResult = Result.fail(500, e.getMessage());
        }

        if (Result.success().getCode() == runResult.getCode()) {
            log.setHandleCode(HttpCode.INTERNAL_SERVER_ERROR.getCode());
            log.setHandleMsg(
                    I18nUtil.getInstance("").getString("joblog_kill_log_byman")
                            + ":"
                            + (runResult.getMessage() != null ? runResult.getMessage() : ""));
            log.setHandleTime(new Date());
            jobLogRepository.save(log);
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
            return Result.fail(500, I18nUtil.getInstance("").getString("joblog_clean_type_invalid"));
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
