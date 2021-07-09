package ltd.fdsa.job.admin.jpa.repository.reader;


import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;
import ltd.fdsa.job.admin.jpa.entity.JobLog;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogReader extends ReadRepository<JobLog, Integer> {
}
