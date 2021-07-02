package ltd.fdsa.job.admin.jpa.service;

import ltd.fdsa.database.service.DataAccessService;
import ltd.fdsa.job.admin.jpa.entity.JobRegistry;

import java.util.Date;
import java.util.List;

public interface JobRegistryService extends DataAccessService<JobRegistry, Integer> {
    List<Integer> findDead(int sss, Date dt);

    List<JobRegistry>  findAll(int sss, Date dt);
}
