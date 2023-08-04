package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.ConnectProvider;
import cn.zhumingwu.client.repository.ConnectProviderRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConnectProviderService {
    @Resource
    ConnectProviderRepository repos;

    public List<ConnectProvider> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(ConnectProvider entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(ConnectProvider entity) {
        return this.repos.update(entity);
    }
}
