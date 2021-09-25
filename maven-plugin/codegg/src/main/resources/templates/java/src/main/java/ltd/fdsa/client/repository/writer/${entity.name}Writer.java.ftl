package ltd.fdsa.client.repository.writer;

import ltd.fdsa.client.jpa.entity.${entity.name};
import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${entity.name}Writer extends WriteRepository<${entity.name}, Integer> {
}
