package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.Post;
import cn.zhumingwu.client.repository.PostRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    @Resource
    PostRepository repos;

    public List<Post> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(Post entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(Post entity) {
        return this.repos.update(entity);
    }
}
