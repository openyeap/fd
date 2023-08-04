package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.UserConnect;
import cn.zhumingwu.client.repository.UserConnectRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserConnectService {
    @Resource
    UserConnectRepository repos;

    public List<UserConnect> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(UserConnect entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(UserConnect entity) {
        return this.repos.update(entity);
    }
}
