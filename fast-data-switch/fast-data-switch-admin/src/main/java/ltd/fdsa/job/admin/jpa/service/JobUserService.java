package ltd.fdsa.job.admin.jpa.service;

import ltd.fdsa.database.service.DataAccessService;
import ltd.fdsa.job.admin.jpa.entity.JobUser;

public interface JobUserService extends DataAccessService<JobUser, Integer> {

    JobUser loadByUserName(String userName);
}
