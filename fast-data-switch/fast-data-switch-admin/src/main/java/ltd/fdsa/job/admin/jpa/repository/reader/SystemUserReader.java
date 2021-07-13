package ltd.fdsa.job.admin.jpa.repository.reader;


import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import ltd.fdsa.job.admin.jpa.entity.SystemUser;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserReader extends ReadRepository<SystemUser, Integer> {
}
