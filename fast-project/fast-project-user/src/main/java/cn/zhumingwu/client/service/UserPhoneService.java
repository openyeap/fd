package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.UserPhone;
import cn.zhumingwu.client.repository.UserPhoneRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserPhoneService {
    @Resource
    UserPhoneRepository repos;

    public List<UserPhone> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(UserPhone entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(UserPhone entity) {
        return this.repos.update(entity);
    }
}
