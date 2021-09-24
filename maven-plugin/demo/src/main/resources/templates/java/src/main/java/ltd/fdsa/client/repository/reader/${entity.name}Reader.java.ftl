package ltd.fdsa.client.repository.reader;


import ltd.fdsa.client.jpa.entity.${entity.name};
import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${entity.name}Reader extends ReadRepository<${entity.name}, Integer> {
}
