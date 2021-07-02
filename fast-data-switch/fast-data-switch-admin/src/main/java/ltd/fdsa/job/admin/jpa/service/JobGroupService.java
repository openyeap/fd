package ltd.fdsa.job.admin.jpa.service;

import ltd.fdsa.database.service.DataAccessService;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;

import java.util.List;

public interface JobGroupService extends DataAccessService<JobGroup, Integer> {

    List<JobGroup> findByAddressType(int test);
}
