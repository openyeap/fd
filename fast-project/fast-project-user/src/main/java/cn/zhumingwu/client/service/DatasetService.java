package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.Dataset;
import cn.zhumingwu.client.repository.DatasetRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DatasetService {
    @Resource
    DatasetRepository repos;

    public List<Dataset> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(Dataset entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(Dataset entity) {
        return this.repos.update(entity);
    }
}
