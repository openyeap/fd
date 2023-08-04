package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.WorkflowInstance;
import cn.zhumingwu.client.repository.WorkflowInstanceRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkflowInstanceService {
    @Resource
    WorkflowInstanceRepository repos;

    public List<WorkflowInstance> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(WorkflowInstance entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(WorkflowInstance entity) {
        return this.repos.update(entity);
    }
}
