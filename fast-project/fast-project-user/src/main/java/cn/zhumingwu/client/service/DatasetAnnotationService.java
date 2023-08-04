package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.DatasetAnnotation;
import cn.zhumingwu.client.repository.DatasetAnnotationRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DatasetAnnotationService {
    @Resource
    DatasetAnnotationRepository repos;

    public List<DatasetAnnotation> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(DatasetAnnotation entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(DatasetAnnotation entity) {
        return this.repos.update(entity);
    }
}
