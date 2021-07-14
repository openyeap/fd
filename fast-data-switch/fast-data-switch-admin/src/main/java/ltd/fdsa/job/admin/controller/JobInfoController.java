package ltd.fdsa.job.admin.controller;

import ltd.fdsa.job.admin.route.ExecutorRouteStrategyEnum;
import ltd.fdsa.job.admin.thread.JobTriggerPoolHelper;
import ltd.fdsa.job.admin.trigger.TriggerTypeEnum;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;
import ltd.fdsa.job.admin.jpa.entity.SystemUser;
import ltd.fdsa.job.admin.jpa.service.JobGroupService;
import ltd.fdsa.job.admin.jpa.service.impl.SystemUserServiceImpl;
import ltd.fdsa.switcher.core.exception.FastDataSwitchException;
import ltd.fdsa.switcher.core.job.cron.CronExpression;
import ltd.fdsa.switcher.core.job.enums.ExecutorBlockStrategyEnum;
import ltd.fdsa.job.admin.util.DateUtil;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.web.view.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * index controller
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {

    @Resource
    private JobGroupService JobGroupDao;
//    @Resource
//    private JobService JobService;

    public static List<JobGroup> filterJobGroupByRole(
            HttpServletRequest request, List<JobGroup> jobGroupList_all) {
        List<JobGroup> jobGroupList = new ArrayList<>();
        if (jobGroupList_all != null && jobGroupList_all.size() > 0) {
            SystemUser loginUser = (SystemUser) request.getAttribute(SystemUserServiceImpl.USER_LOGIN_IDENTITY);
            if (loginUser.getType() == 1) {
                jobGroupList = jobGroupList_all;
            } else {
                List<String> groupIdStrs = new ArrayList<>();
                if (loginUser.getPermission() != null && loginUser.getPermission().trim().length() > 0) {
                    groupIdStrs = Arrays.asList(loginUser.getPermission().trim().split(","));
                }
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
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

        // 枚举-字典
        model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values()); // 路由策略-列表
        model.addAttribute(
                "ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values()); // 阻塞处理策略-字典

        // 执行器列表
        List<JobGroup> jobGroupList_all = JobGroupDao.findAll();

        // filter group
        List<JobGroup> jobGroupList = filterJobGroupByRole(request, jobGroupList_all);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new FastDataSwitchException(I18nUtil.getString("jobgroup_empty"));
        }

        model.addAttribute("JobGroupList", jobGroupList);
        model.addAttribute("jobGroup", jobGroup);

        return "jobinfo/jobinfo.index";
    }

//    @RequestMapping("/pageList")
//    @ResponseBody
//    public Map<String, Object> pageList(
//            @RequestParam(required = false, defaultValue = "0") int start,
//            @RequestParam(required = false, defaultValue = "10") int length,
//            int jobGroup,
//            int triggerStatus,
//            String jobDesc,
//            String executorHandler,
//            String author) {
//
//        return JobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
//    }

//    @RequestMapping("/add")
//    @ResponseBody
//    public Result<String> add(JobInfo jobInfo) {
//        return JobService.add(jobInfo);
//    }
//
//    @RequestMapping("/update")
//    @ResponseBody
//    public Result<String> update(JobInfo jobInfo) {
//        return JobService.update(jobInfo);
//    }
//
//    @RequestMapping("/remove")
//    @ResponseBody
//    public Result<String> remove(int id) {
//        return JobService.remove(id);
//    }
//
//    @RequestMapping("/stop")
//    @ResponseBody
//    public Result<String> pause(int id) {
//        return JobService.stop(id);
//    }
//
//    @RequestMapping("/start")
//    @ResponseBody
//    public Result<String> start(int id) {
//        return JobService.start(id);
//    }

    @RequestMapping("/trigger")
    @ResponseBody
    // @PermissionLimit(limit = false)
    public Result<String> triggerJob(int id, String executorParam) {
        // force cover job param
        if (executorParam == null) {
            executorParam = "";
        }

        JobTriggerPoolHelper.trigger(id, TriggerTypeEnum.MANUAL, -1, null, executorParam);
        return Result.success();
    }

    @RequestMapping("/nextTriggerTime")
    @ResponseBody
    public Result<List<String>> nextTriggerTime(String cron) {
        List<String> result = new ArrayList<>();
        try {
            CronExpression cronExpression = new CronExpression(cron);
            Date lastTime = new Date();
            for (int i = 0; i < 5; i++) {
                lastTime = cronExpression.getNextValidTimeAfter(lastTime);
                if (lastTime != null) {
                    result.add(DateUtil.formatDateTime(lastTime));
                } else {
                    break;
                }
            }
        } catch (ParseException e) {
            return Result.fail(500, I18nUtil.getString("jobinfo_field_cron_invalid"));
        }
        return Result.success(result);
    }
}
