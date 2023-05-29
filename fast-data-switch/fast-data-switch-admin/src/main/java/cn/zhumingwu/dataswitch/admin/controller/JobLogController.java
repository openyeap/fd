package cn.zhumingwu.dataswitch.admin.controller;

import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.repository.JobLogRepository;
import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import cn.zhumingwu.dataswitch.admin.entity.JobLog;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.DateUtil;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * index controller
 */
@Controller
@RequestMapping("/joblog")
public class JobLogController {
    private static Logger logger = LoggerFactory.getLogger(JobLogController.class);


    @Resource
    public JobInfoRepository JobInfoDao;
    @Resource
    public JobLogRepository jobLogRepository;


    @RequestMapping
    public String index(Model model, @RequestParam(required = false, defaultValue = "0") Long jobId) {

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

        model.addAttribute("logId", jobLog.getId());
        return "joblog/joblog.detail";
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

        return Result.success();
    }
}
