package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.ClientDepartment;
import cn.zhumingwu.client.repository.ClientDepartmentRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientDepartmentService {
    @Resource
    ClientDepartmentRepository repos;

    public List<ClientDepartment> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(ClientDepartment entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(ClientDepartment entity) {
        return this.repos.update(entity);
    }
}
