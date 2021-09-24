package ltd.fdsa.client.repository.writer;

import ltd.fdsa.client.jpa.entity.UserExt;
import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExtWriter extends WriteRepository<UserExt, Integer> {
}
