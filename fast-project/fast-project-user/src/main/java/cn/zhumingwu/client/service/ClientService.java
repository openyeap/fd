package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.Client;
import cn.zhumingwu.client.repository.ClientRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {
    @Resource
    ClientRepository repos;

    public List<Client> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(Client entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(Client entity) {
        return this.repos.update(entity);
    }
}
