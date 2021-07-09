package ltd.fdsa.job.admin.jpa.service.impl;

import ltd.fdsa.database.jpa.service.BaseJpaService;
import ltd.fdsa.job.admin.jpa.entity.JobRegistry;
import ltd.fdsa.job.admin.jpa.repository.reader.JobRegistryReader;
import ltd.fdsa.job.admin.jpa.repository.writer.JobRegistryWriter;
import ltd.fdsa.job.admin.jpa.service.JobRegistryService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JobRegistryServiceImpl extends BaseJpaService<JobRegistry, Integer, JobRegistryWriter, JobRegistryReader> implements JobRegistryService {


    @Override
    public List<Integer> findDead(int sss, Date dt) {
        return null;
    }

    @Override
    public List<JobRegistry> findAll(int sss, Date dt) {
        return null;
    }
}
