package ltd.fdsa.job.admin.jpa.repository.writer;

import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogWriter extends WriteRepository<JobGroup, Integer> {
}
