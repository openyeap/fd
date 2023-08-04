package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.UserClient;
import cn.zhumingwu.client.repository.UserClientRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserClientService {
    @Resource
    UserClientRepository repos;

    public List<UserClient> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(UserClient entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(UserClient entity) {
        return this.repos.update(entity);
    }
}
