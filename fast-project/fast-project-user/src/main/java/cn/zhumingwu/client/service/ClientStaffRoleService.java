package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.ClientStaffRole;
import cn.zhumingwu.client.repository.ClientStaffRoleRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientStaffRoleService {
    @Resource
    ClientStaffRoleRepository repos;

    public List<ClientStaffRole> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(ClientStaffRole entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(ClientStaffRole entity) {
        return this.repos.update(entity);
    }
}
