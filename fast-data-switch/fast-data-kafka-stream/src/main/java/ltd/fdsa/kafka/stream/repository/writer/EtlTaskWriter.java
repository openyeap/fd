package ltd.fdsa.kafka.stream.repository.writer;

import ltd.fdsa.database.jpa.repository.writer.WriteRepository;
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
public interface EtlTaskWriter extends WriteRepository<EtlTask, Integer> {

}
