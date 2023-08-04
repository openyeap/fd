package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.ClientRole;
import cn.zhumingwu.client.repository.ClientRoleRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientRoleService {
    @Resource
    ClientRoleRepository repos;

    public List<ClientRole> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(ClientRole entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(ClientRole entity) {
        return this.repos.update(entity);
    }
}
