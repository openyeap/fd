package ltd.fdsa.job.admin.jpa.repository.reader;


import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import ltd.fdsa.job.admin.jpa.entity.JobInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInfoReader extends ReadRepository<JobInfo, Integer> {
}
