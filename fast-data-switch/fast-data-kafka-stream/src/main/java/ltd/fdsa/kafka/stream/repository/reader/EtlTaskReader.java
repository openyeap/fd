package ltd.fdsa.kafka.stream.repository.reader;

import ltd.fdsa.database.jpa.repository.reader.ReadRepository;
import ltd.fdsa.kafka.stream.entity.EtlTask;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数据访问类
 * </p>
 *
 * @author Pivotal
 * @since 2021-04-06
 */
@Repository
public interface EtlTaskReader extends ReadRepository<EtlTask,Integer> {

}
