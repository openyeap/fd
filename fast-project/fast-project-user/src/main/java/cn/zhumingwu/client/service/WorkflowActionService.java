package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.WorkflowAction;
import cn.zhumingwu.client.repository.WorkflowActionRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkflowActionService {
    @Resource
    WorkflowActionRepository repos;

    public List<WorkflowAction> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(WorkflowAction entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(WorkflowAction entity) {
        return this.repos.update(entity);
    }
}
