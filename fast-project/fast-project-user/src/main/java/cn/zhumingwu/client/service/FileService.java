package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.File;
import cn.zhumingwu.client.repository.FileRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FileService {
    @Resource
    FileRepository repos;

    public List<File> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(File entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(File entity) {
        return this.repos.update(entity);
    }
}
