package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.UserCard;
import cn.zhumingwu.client.repository.UserCardRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserCardService {
    @Resource
    UserCardRepository repos;

    public List<UserCard> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(UserCard entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(UserCard entity) {
        return this.repos.update(entity);
    }
}
