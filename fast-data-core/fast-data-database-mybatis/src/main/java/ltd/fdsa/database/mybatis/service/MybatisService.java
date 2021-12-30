package ltd.fdsa.database.mybatis.service;

import ltd.fdsa.database.entity.BaseEntity;
import ltd.fdsa.database.mybatis.mapper.reader.ReadMapper;
import ltd.fdsa.database.mybatis.mapper.writer.WriteMapper;
import ltd.fdsa.database.service.DataAccessService;
import ltd.fdsa.database.sql.conditions.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class MybatisService<Entity extends BaseEntity<ID>, ID, Writer extends WriteMapper<Entity>, Reader extends ReadMapper<Entity>> implements DataAccessService<Entity, ID> {

    @Autowired
    protected Writer writer;

    @Autowired
    protected Reader reader;

    @Override
    public Optional<Entity> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public List<Entity> findAll() {
        return null;
    }

    @Override
    public List<Entity> findAllById(ID... ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Page<Entity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public List<Entity> find(Condition where) {
        return null;
    }

    @Override
    public Entity update(Entity entity) {
        return null;
    }

    @Override
    public void updateAll(Entity... entities) {
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void deleteAll(Entity...entities) {

    }

    @Override
    public void clearAll() {

    }

    @Override
    public Entity insert(Entity entity) {
        return null;
    }

    @Override
    public void insertAll(Entity... entities) {

    }

    @Override
    public int executeSql(String sql) {
        return 0;
    }
}
