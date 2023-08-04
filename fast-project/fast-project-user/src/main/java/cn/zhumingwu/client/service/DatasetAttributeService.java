package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.DatasetAttribute;
import cn.zhumingwu.client.repository.DatasetAttributeRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DatasetAttributeService {
    @Resource
    DatasetAttributeRepository repos;

    public List<DatasetAttribute> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(DatasetAttribute entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(DatasetAttribute entity) {
        return this.repos.update(entity);
    }
}
