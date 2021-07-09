package ltd.fdsa.job.admin.jpa.repository.writer;

import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;
import ltd.fdsa.job.admin.jpa.entity.JobRegistry;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRegistryWriter extends WriteRepository<JobRegistry, Integer> {
}
