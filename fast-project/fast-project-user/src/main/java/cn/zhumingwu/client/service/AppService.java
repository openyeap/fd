package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.App;
import cn.zhumingwu.client.repository.AppRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppService {
    @Resource
    AppRepository repos;

    public List<App> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(App entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(App entity) {
        return this.repos.update(entity);
    }
}
