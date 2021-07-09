package ltd.fdsa.job.admin.jpa.service.impl;

import lombok.var;
import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.JobLogReport;
import ltd.fdsa.job.admin.jpa.entity.JobUser;
import ltd.fdsa.job.admin.jpa.repository.reader.JobLogReportReader;
import ltd.fdsa.job.admin.jpa.repository.reader.JobUserReader;
import ltd.fdsa.job.admin.jpa.repository.writer.JobLogReportWriter;
import ltd.fdsa.job.admin.jpa.repository.writer.JobUserWriter;
import ltd.fdsa.job.admin.jpa.service.JobLogReportService;
import ltd.fdsa.job.admin.jpa.service.JobUserService;
import ltd.fdsa.job.admin.util.CookieUtil;
import ltd.fdsa.job.admin.util.I18nUtil;
import ltd.fdsa.switcher.core.util.JacksonUtil;
import ltd.fdsa.web.view.Result;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@Service
public class JobLogReportServiceImpl extends BaseJpaService<JobLogReport, Integer, JobLogReportWriter, JobLogReportReader> implements JobLogReportService {

}
