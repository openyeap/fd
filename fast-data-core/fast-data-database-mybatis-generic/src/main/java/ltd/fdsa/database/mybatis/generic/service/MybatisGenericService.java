package ltd.fdsa.database.mybatis.generic.service;

import lombok.var;
import ltd.fdsa.database.entity.BaseEntity;
import ltd.fdsa.database.mybatis.generic.mapper.reader.ReadMapper;
import ltd.fdsa.database.mybatis.generic.mapper.writer.WriteMapper;
import ltd.fdsa.database.service.DataAccessService;
import ltd.fdsa.database.sql.conditions.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MybatisGenericService<Entity extends BaseEntity<ID>, ID, Writer extends WriteMapper<Entity>, Reader extends ReadMapper<Entity>> implements DataAccessService<Entity, ID> {

    @Autowired
    protected Writer writer;

    @Autowired
    protected Reader reader;

    @Override
    public Optional<Entity> findById(ID id) {
        return Optional.of(this.reader.selectByPrimaryKey(id));
    }

    @Override
    public List<Entity> findAll() {
        return this.reader.selectAll();
    }

    @Override
    public List<Entity> findAllById(ID... ids) {
        return this.reader.selectByExample(null);
    }

    @Override
    public long count() {
        return this.reader.selectCountByExample(null);
    }

    @Override
    public Page<Entity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsById(ID id) {
        return this.reader.existsWithPrimaryKey(id);
    }

    @Override
    public List<Entity> find(Condition where) {
        return Collections.emptyList();
    }

    @Override
    public Entity update(Entity entity) {
        this.writer.updateByPrimaryKey(entity);
        return entity;
    }

    @Override
    public void updateAll(Entity... entities) {
        for (var entity : entities) {
            this.writer.updateByPrimaryKey(entity);
        }
    }

    @Override
    public void deleteById(ID id) {
        this.writer.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteAll(Entity... entities) {
        for (var entity : entities) {
            this.writer.delete(entity);
        }
    }

    void insertFill(Entity entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var userId = (ID) request.getHeader("X_USER_ID");
        entity.setCreateBy(userId);
        entity.setUpdateBy(userId);
    }

    void updateFill(Entity entity) {
        entity.setUpdateTime(LocalDateTime.now());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var userId = (ID) request.getHeader("X_USER_ID");
        entity.setUpdateBy(userId);
    }
    @Override
    public void clearAll() {
        Example example = Example.builder(BaseEntity.class).build();
        this.writer.deleteByExample(example);
    }

    @Override
    public Entity insert(Entity entity) {
        this.writer.insert(entity);
        return entity;
    }

    @Override
    public void insertAll(Entity... entities) {
        for (var entity : entities) {
            this.writer.insert(entity);
        }
    }
}
