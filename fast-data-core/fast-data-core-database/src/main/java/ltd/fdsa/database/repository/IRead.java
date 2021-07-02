package ltd.fdsa.database.repository;

import ltd.fdsa.database.entity.BaseEntity;
import ltd.fdsa.database.sql.queries.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author
 * @since 3/20/2021 10:35 AM
 */
public interface IRead<Entity extends BaseEntity, ID> {
    Optional<Entity> findById(ID id);

    List<Entity> findAll();

    List<Entity> findAllById(ID... ids);

    long count();

    Page<Entity> findAll(Pageable pageable);

    boolean existsById(ID id);

    List<Entity> findWhere(Select select);
}