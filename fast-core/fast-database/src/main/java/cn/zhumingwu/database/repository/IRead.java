package cn.zhumingwu.database.repository;

import cn.zhumingwu.database.entity.BaseEntity;
import cn.zhumingwu.database.sql.conditions.Condition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author zhumingwu
 * @since 3/20/2021 10:35 AM
 */
public interface IRead<Entity extends BaseEntity<ID>, ID> {
    Optional<Entity> findById(ID id);

    List<Entity> findAll();

    List<Entity> findAllById(ID... ids);

    long count();

    Page<Entity> findAll(Pageable pageable);

    boolean existsById(ID id);

    List<Entity> find(Condition where);
}