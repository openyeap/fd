package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.ClientMenu;
import cn.zhumingwu.client.repository.ClientMenuRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientMenuService {
    @Resource
    ClientMenuRepository repos;

    public List<ClientMenu> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(ClientMenu entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(ClientMenu entity) {
        return this.repos.update(entity);
    }
}
