package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.WorkflowForm;
import cn.zhumingwu.client.repository.WorkflowFormRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkflowFormService {
    @Resource
    WorkflowFormRepository repos;

    public List<WorkflowForm> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(WorkflowForm entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(WorkflowForm entity) {
        return this.repos.update(entity);
    }
}
