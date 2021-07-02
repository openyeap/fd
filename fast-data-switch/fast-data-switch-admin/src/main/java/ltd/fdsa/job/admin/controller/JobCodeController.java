package ltd.fdsa.job.admin.controller;


import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import ltd.fdsa.job.admin.jpa.entity.JobTask;
import ltd.fdsa.job.admin.jpa.service.JobInfoService;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * job code controller
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {

    @Autowired
    JobInfoService JobInfoDao;

    @RequestMapping
    public String index(HttpServletRequest request, Model model, int jobId) {
        JobInfo jobInfo = JobInfoDao.findById(jobId).get();
//        List<JobLogGlue> jobLogGlues = JobLogGlueDao.findByJobId(jobId);

        if (jobInfo == null) {
            throw new RuntimeException(I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }
        // valid permission
        JobInfoController.validPermission(request, jobInfo.getGroupId());
        model.addAttribute("jobInfo", jobInfo);
        return "jobcode/jobcode.index";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Result<String> save(Model model, int id, String glueSource, String glueRemark) {
        // valid
        if (glueRemark == null) {
            return Result.fail(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")));
        }
        if (glueRemark.length() < 4 || glueRemark.length() > 100) {
            return Result.fail(500, I18nUtil.getString("jobinfo_glue_remark_limit"));
        }
        JobInfo exists_jobInfo = JobInfoDao.findById(id).get();
        if (exists_jobInfo == null) {
            return Result.fail(500, I18nUtil.getString("jobinfo_glue_jobid_unvalid"));
        }


        JobInfoDao.update(exists_jobInfo);

        // log old code
        JobTask JobLogGlue = new JobTask();
        JobLogGlue.setJobId(exists_jobInfo.getId());
        JobLogGlue.setGlueSource(glueSource);
        JobLogGlue.setGlueRemark(glueRemark);

//        JobLogGlueDao.save(JobLogGlue);
        // remove code backup more than 30
//        JobLogGlueDao.removeOld(exists_jobInfo.getId(), 30);

        return Result.success();
    }
}
