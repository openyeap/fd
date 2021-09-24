package ltd.fdsa.client.repository.writer;

import ltd.fdsa.client.jpa.entity.User;
import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWriter extends WriteRepository<User, Integer> {
}
