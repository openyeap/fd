package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.WorkflowActor;
import cn.zhumingwu.client.repository.WorkflowActorRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkflowActorService {
    @Resource
    WorkflowActorRepository repos;

    public List<WorkflowActor> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(WorkflowActor entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(WorkflowActor entity) {
        return this.repos.update(entity);
    }
}
