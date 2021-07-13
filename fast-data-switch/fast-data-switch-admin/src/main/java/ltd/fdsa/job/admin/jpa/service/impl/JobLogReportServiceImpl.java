package ltd.fdsa.job.admin.jpa.service.impl;

import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.JobLogReport;
import ltd.fdsa.job.admin.jpa.repository.reader.JobLogReportReader;
import ltd.fdsa.job.admin.jpa.repository.writer.JobLogReportWriter;
import ltd.fdsa.job.admin.jpa.service.JobLogReportService;
import org.springframework.stereotype.Service;

@Service
public class JobLogReportServiceImpl extends BaseJpaService<JobLogReport, Integer, JobLogReportWriter, JobLogReportReader> implements JobLogReportService {

}
