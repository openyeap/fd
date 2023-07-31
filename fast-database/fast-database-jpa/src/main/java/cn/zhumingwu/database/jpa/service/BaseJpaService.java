package cn.zhumingwu.database.jpa.service;

import cn.zhumingwu.database.entity.BaseEntity;
import cn.zhumingwu.database.entity.Status;
import cn.zhumingwu.database.jpa.repository.reader.ReadRepository;
import cn.zhumingwu.database.jpa.repository.writer.WriteRepository;
import cn.zhumingwu.database.service.DataAccessService;
import cn.zhumingwu.database.sql.conditions.Condition;
import com.google.common.collect.Lists;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BaseJpaService<Entity extends BaseEntity<ID>, ID, Writer extends WriteRepository<Entity, ID>, Reader extends ReadRepository<Entity, ID>> implements DataAccessService<Entity, ID> {

    @Autowired
    protected WriteRepository<Entity, ID> writer;

    @Autowired
    protected ReadRepository<Entity, ID> reader;

    @Override
    public Optional<Entity> findById(ID id) {
        return this.reader.findById(id);
    }

    @Override
    public List<Entity> findAll() {
        return this.reader.findAll();
    }

    @Override
    public long count() {
        return this.reader.count();
    }

    @Override
    public List<Entity> findAllById(ID... ids) {
        return null;
    }


    @Override
    public Page<Entity> findAll(Pageable pageable) {
        return this.reader.findAll(pageable);
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public List<Entity> find(Condition where) {
        return Collections.emptyList();
    }


    @Override
    public Entity update(Entity entity) {
        var result = this.writer.findById(entity.getId());
        if (result.isPresent()) {
            var target = result.get();
            BeanUtils.copyProperties(entity, target, "createTime", "createBy");
            return this.writer.save(target);
        }
        return null;
    }


    @Override
    public void deleteAll(Entity... entities) {
        for (var entity : entities) {
            entity.setStatus(Status.DELETE);

        }
        this.writer.saveAll(Lists.newArrayList(entities));
    }

    @Override
    public void updateAll(Entity... entities) {
        this.writer.saveAll(Lists.newArrayList(entities));
    }

    @Override
    public void deleteById(ID id) {
        var result = this.writer.findById(id);
        if (result.isPresent() && result.get().getStatus() != Status.DELETE) {
            var entity = result.get();
            entity.setStatus(Status.DELETE);
            this.writer.saveAndFlush(entity);
        }
    }

    @Override
    public void clearAll() {
        this.writer.deleteAllInBatch();
    }

    @Override
    public Entity insert(Entity entity) {
        return this.writer.save(entity);
    }

    @Override
    public void insertAll(Entity... entities) {
        var list = Lists.newArrayList(entities);
        this.writer.saveAll(list);
    }
}
