package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.Blob;
import cn.zhumingwu.client.repository.BlobRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BlobService {
    @Resource
    BlobRepository repos;

    public List<Blob> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(Blob entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(Blob entity) {
        return this.repos.update(entity);
    }
}
