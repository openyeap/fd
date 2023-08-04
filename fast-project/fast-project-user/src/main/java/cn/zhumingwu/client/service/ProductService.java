package cn.zhumingwu.client.service;

import cn.zhumingwu.client.entity.Product;
import cn.zhumingwu.client.repository.ProductRepository;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Resource
    ProductRepository repos;

    public List<Product> findAll(String where, Object... param) {
        return this.repos.query(where,param);
    }

    public int insert(Product entity) {
        return this.repos.insert(entity);
    }

    public int deleteById(Integer id) {
        return this.repos.delete(id);
    }

    public int update(Product entity) {
        return this.repos.update(entity);
    }
}
