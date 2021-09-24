package ltd.fdsa.client.jpa.repository.reader;


import ltd.fdsa.client.jpa.entity.Group;
import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupReader extends ReadRepository<Group, Integer> {
}
