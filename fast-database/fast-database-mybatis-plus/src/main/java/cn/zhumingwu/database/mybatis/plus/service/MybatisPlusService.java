package cn.zhumingwu.database.mybatis.plus.service;

import cn.zhumingwu.database.entity.BaseEntity;
import cn.zhumingwu.database.entity.Status;
import cn.zhumingwu.database.mybatis.plus.mapper.reader.ReadMapper;
import cn.zhumingwu.database.mybatis.plus.mapper.writer.WriteMapper;
import cn.zhumingwu.database.service.DataAccessService;
import cn.zhumingwu.database.sql.conditions.Condition;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MybatisPlusService<Entity extends BaseEntity<ID>, ID, Writer extends WriteMapper<Entity>, Reader extends ReadMapper<Entity>> implements DataAccessService<Entity, ID> {

    @Autowired
    protected Writer writer;

    @Autowired
    protected Reader reader;


    @Override
    public Optional<Entity> findById(ID id) {
        return Optional.of(this.reader.selectById(id.toString()));
    }

    @Override
    public List<Entity> findAll() {
        return this.reader.selectList(null);
    }

    @Override
    public List<Entity> findAllById(ID... ids) {
        return null;
    }

    @Override
    public long count() {
        return this.reader.selectCount(null);
    }

    @Override
    public Page<Entity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsById(ID id) {
        return this.reader.selectById(id.toString()) != null;
    }

    @Override
    public List<Entity> find(Condition where) {
        return Collections.emptyList();
    }

    @Override
    public Entity update(Entity entity) {
        updateFill(entity);
        this.writer.updateById(entity);
        return entity;
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
    public void updateAll(Entity... entities) {
        for (var entity : entities) {
            this.updateFill(entity);
            this.writer.updateById(entity);
        }
    }

    @Override
    public void deleteById(ID id) {
        var entity = this.writer.selectById(id.toString());
        if (entity != null && entity.getStatus() != Status.DELETE) {
            deleteAll(entity);
        }
    }

    @Override
    public void deleteAll(Entity... entities) {
        for (var entity : entities) {
            entity.setStatus(Status.DELETE);
            updateFill(entity);
            this.writer.updateById(entity);
        }
    }

    @Override
    public void clearAll() {
        this.writer.delete(null);
    }

    @Override
    public Entity insert(Entity entity) {
        insertFill(entity);
        this.writer.insert(entity);
        return entity;
    }

    @Override
    public void insertAll(Entity... entities) {
        for (var entity : entities) {
            insertFill(entity);
            this.writer.insert(entity);
        }
    }
}
