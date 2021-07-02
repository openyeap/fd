package ltd.fdsa.client.jpa.repository.writer;

import ltd.fdsa.client.jpa.entity.Group;
import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupWriter extends WriteRepository<Group, Integer> {
}
