package ltd.fdsa.job.admin.jpa.service.impl;

import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;
import ltd.fdsa.job.admin.jpa.repository.reader.JobGroupReader;
import ltd.fdsa.job.admin.jpa.repository.writer.JobGroupWriter;
import ltd.fdsa.job.admin.jpa.service.JobGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobGroupServiceImpl extends BaseJpaService<JobGroup, Integer, JobGroupWriter, JobGroupReader> implements JobGroupService {


    @Override
    public List<JobGroup> findByAddressType(int test) {
        return null;
    }
}
