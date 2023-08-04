package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.UploadFile;
import cn.zhumingwu.client.repository.UploadFileRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UploadFileService {
    @Resource
    UploadFileRepository repos;

    public List<UploadFile> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(UploadFile entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(UploadFile entity) {
        return this.repos.update(entity);
    }
}
