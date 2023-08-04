package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.User;
import cn.zhumingwu.client.repository.UserRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserRepository repos;

    public List<User> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(User entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(User entity) {
        return this.repos.update(entity);
    }
}
