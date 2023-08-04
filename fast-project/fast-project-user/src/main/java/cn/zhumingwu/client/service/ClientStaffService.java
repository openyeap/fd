package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.ClientStaff;
import cn.zhumingwu.client.repository.ClientStaffRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientStaffService {
    @Resource
    ClientStaffRepository repos;

    public List<ClientStaff> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(ClientStaff entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(ClientStaff entity) {
        return this.repos.update(entity);
    }
}
