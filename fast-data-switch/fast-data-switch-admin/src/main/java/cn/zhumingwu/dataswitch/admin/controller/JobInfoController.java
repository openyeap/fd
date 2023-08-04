package cn.zhumingwu.dataswitch.admin.controller;

import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.dataswitch.admin.context.CoordinatorContext;
import cn.zhumingwu.dataswitch.core.job.cron.CronExpression;
import cn.zhumingwu.dataswitch.core.job.enums.ExecutorBlockStrategy;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.DateUtil;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;
import cn.zhumingwu.dataswitch.admin.enums.ExecutorRouteStrategy;
import cn.zhumingwu.dataswitch.admin.enums.TriggerType;
import com.google.common.base.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * index controller
 */
@Controller
@RequestMapping("job")
public class JobInfoController extends BaseController {


    @Resource
    private CoordinatorContext context;


    @RequestMapping
    public String index(Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

        // 枚举-字典
        model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategy.values()); // 路由策略-列表
        model.addAttribute(
                "ExecutorBlockStrategyEnum", ExecutorBlockStrategy.values()); // 阻塞处理策略-字典

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
    public Result<String> triggerJob(Long id, String expression, String param, Integer retryTimes) {
        if (id == null) {
            return Result.fail(HttpCode.NOT_FOUND);
        }
        List<String> executorParam;
        if (Strings.isNullOrEmpty(param)) {
            executorParam = Collections.emptyList();
        } else {
            executorParam = Arrays.stream(param.split(",")).filter(m -> !Strings.isNullOrEmpty(m)).collect(Collectors.toList());
        }
        List<String> executorExpression;
        if (Strings.isNullOrEmpty(expression)) {
            executorExpression = Collections.emptyList();
        } else {
            executorExpression = Arrays.stream(expression.split(",")).filter(m -> !Strings.isNullOrEmpty(m)).collect(Collectors.toList());
        }
        context.trigger(id, TriggerType.MANUAL, executorExpression, executorParam, retryTimes);
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
