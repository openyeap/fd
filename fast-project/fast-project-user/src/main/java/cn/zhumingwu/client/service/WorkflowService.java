package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.Workflow;
import cn.zhumingwu.client.repository.WorkflowRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkflowService {
    @Resource
    WorkflowRepository repos;

    public List<Workflow> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(Workflow entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(Workflow entity) {
        return this.repos.update(entity);
    }
}
