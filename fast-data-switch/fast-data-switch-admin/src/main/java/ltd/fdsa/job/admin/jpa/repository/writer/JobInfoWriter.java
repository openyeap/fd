package ltd.fdsa.job.admin.jpa.repository.writer;

import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInfoWriter extends WriteRepository<JobInfo, Integer> {
}
