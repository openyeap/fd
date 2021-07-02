package ltd.fdsa.job.admin.jpa.repository.reader;


import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import ltd.fdsa.job.admin.jpa.entity.JobUser;
import org.springframework.stereotype.Repository;

@Repository
public interface JobUserReader extends ReadRepository<JobUser, Integer> {
}
