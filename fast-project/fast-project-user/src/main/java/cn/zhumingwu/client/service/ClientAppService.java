package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.ClientApp;
import cn.zhumingwu.client.repository.ClientAppRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientAppService {
    @Resource
    ClientAppRepository repos;

    public List<ClientApp> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(ClientApp entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(ClientApp entity) {
        return this.repos.update(entity);
    }
}
