package cn.zhumingwu.dataswitch.admin.controller;

import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.core.exception.FastDataSwitchException;
import cn.zhumingwu.dataswitch.core.job.cron.CronExpression;
import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.DateUtil;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import cn.zhumingwu.dataswitch.admin.entity.User;
import cn.zhumingwu.dataswitch.admin.enums.ExecutorRouteStrategy;
import cn.zhumingwu.dataswitch.admin.service.impl.SystemUserService;
import cn.zhumingwu.dataswitch.admin.thread.JobTriggerPoolHelper;
import cn.zhumingwu.dataswitch.admin.enums.TriggerTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * index controller
 */
@Controller
@RequestMapping("job")
public class JobInfoController extends BaseController {

    @Resource
    private JobGroupRepository jobGroupService;

    @Autowired
    private SystemUserService userService;

    List<JobGroup> filterJobGroupByRole(List<JobGroup> jobGroupList_all) {
        List<JobGroup> jobGroupList = new ArrayList<>();
        if (jobGroupList_all != null && jobGroupList_all.size() > 0) {
            User loginUser = this.userService.checkLogin();
            if (loginUser.getType() == 1) {
                jobGroupList = jobGroupList_all;
            } else {
                List<String> list = new ArrayList<>();

                for (JobGroup groupItem : jobGroupList_all) {
                    if (list.contains(String.valueOf(groupItem.getId()))) {
                        jobGroupList.add(groupItem);
                    }
                }
            }
        }
        return jobGroupList;
    }

    @RequestMapping
    public String index(Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

        // 枚举-字典
        model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategy.values()); // 路由策略-列表
        model.addAttribute(
                "ExecutorBlockStrategyEnum", ExecutorBlockStrategy.values()); // 阻塞处理策略-字典

        // 执行器列表
        List<JobGroup> jobGroupList_all = jobGroupService.findAll();

        // filter group
        List<JobGroup> jobGroupList = filterJobGroupByRole(jobGroupList_all);
        if (jobGroupList == null || jobGroupList.size() == 0) {
            throw new FastDataSwitchException(I18nUtil.getInstance("").getString("jobgroup_empty"));
        }

        model.addAttribute("JobGroupList", jobGroupList);
        model.addAttribute("jobGroup", jobGroup);

        return "job/index";
    }

    @RequestMapping(method = RequestMethod.GET, path = "list")
    public String list(Model model) {
        return "job/list";
    }


    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Result<Object> add(@RequestBody Map<String, Object> data) {
        return Result.success(data);

    }
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
    public Result<String> triggerJob(Long id, String executorParam) {
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
            return Result.fail(500, I18nUtil.getInstance("").getString("jobinfo_field_cron_invalid"));
        }
        return Result.success(result);
    }
}
