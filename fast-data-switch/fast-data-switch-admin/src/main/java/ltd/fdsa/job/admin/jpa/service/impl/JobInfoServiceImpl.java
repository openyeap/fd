package ltd.fdsa.job.admin.jpa.service.impl;

import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import ltd.fdsa.job.admin.jpa.repository.reader.JobInfoReader;
import ltd.fdsa.job.admin.jpa.repository.writer.JobInfoWriter;
import ltd.fdsa.job.admin.jpa.service.JobInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobInfoServiceImpl extends BaseJpaService<JobInfo, Integer, JobInfoWriter, JobInfoReader> implements JobInfoService {
    private static Logger logger = LoggerFactory.getLogger(JobInfoServiceImpl.class);


}
