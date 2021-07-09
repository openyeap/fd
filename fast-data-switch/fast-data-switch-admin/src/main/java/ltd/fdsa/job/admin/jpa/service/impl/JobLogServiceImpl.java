package ltd.fdsa.job.admin.jpa.service.impl;

import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.JobLog;
import ltd.fdsa.job.admin.jpa.repository.reader.JobLogReader;
import ltd.fdsa.job.admin.jpa.repository.writer.JobLogWriter;
import ltd.fdsa.job.admin.jpa.service.JobLogService;
import org.springframework.stereotype.Service;

@Service
public class JobLogServiceImpl extends BaseJpaService<JobLog, Integer, JobLogWriter, JobLogReader> implements JobLogService {

}
