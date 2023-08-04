package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.UserEmail;
import cn.zhumingwu.client.repository.UserEmailRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserEmailService {
    @Resource
    UserEmailRepository repos;

    public List<UserEmail> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(UserEmail entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(UserEmail entity) {
        return this.repos.update(entity);
    }
}
