package cn.zhumingwu.dataswitch.admin.controller;


import cn.zhumingwu.dataswitch.admin.repository.JobInfoRepository;
import cn.zhumingwu.dataswitch.admin.entity.JobInfo;
import cn.zhumingwu.dataswitch.admin.entity.JobTask;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.util.I18nUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * job code controller
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {

    @Autowired
    JobInfoRepository JobInfoDao;

    @RequestMapping
    public String index(Model model, Long jobId) {
        JobInfo jobInfo = JobInfoDao.findById(jobId).get();
//        List<JobLogGlue> jobLogGlues = JobLogGlueDao.findByJobId(jobId);

        if (jobInfo == null) {
            throw new RuntimeException(I18nUtil.getInstance("").getString("jobinfo_glue_jobid_invalid"));
        }

        model.addAttribute("jobInfo", jobInfo);
        return "jobcode/jobcode.index";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Result<String> save(Model model, Long id, String glueSource, String glueRemark) {
        // valid
        if (glueRemark == null) {
            return Result.fail(500, (I18nUtil.getInstance("").getString("system_please_input") + I18nUtil.getInstance("").getString("jobinfo_glue_remark")));
        }
        if (glueRemark.length() < 4 || glueRemark.length() > 100) {
            return Result.fail(500, I18nUtil.getInstance("").getString("jobinfo_glue_remark_limit"));
        }
        JobInfo exists_jobInfo = JobInfoDao.findById(id).get();
        if (exists_jobInfo == null) {
            return Result.fail(500, I18nUtil.getInstance("").getString("jobinfo_glue_jobid_invalid"));
        }


        JobInfoDao.save(exists_jobInfo);

        // log old code
        JobTask jobTask = new JobTask();
        jobTask.setJobId(exists_jobInfo.getId());

        return Result.success();
    }
}
