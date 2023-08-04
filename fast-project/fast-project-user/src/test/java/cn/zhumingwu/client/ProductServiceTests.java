package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ProductService;
import cn.zhumingwu.client.entity.Product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author zhumingwu
 * @ClassName:
 * @description:
 * @since 2020-12-09
 **/
@SpringBootTest
@ContextConfiguration(classes = {UserApplication.class})
@Slf4j
public class ProductServiceTests {

    @Autowired
    ProductService service;


    /***
    * 查询所有
    */
    @Test
    public void TestProductQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertProduct() {
        Product entity = new Product();
        entity.setId(555L);
        entity.setClientId(555L);
        entity.setParentId(555L);
        entity.setCode("test");
        entity.setName("test");
        entity.setSummary("test");
        entity.setDetail("test");
        entity.setImages("test");
        entity.setVideos("test");
        entity.setUnit("test");
        entity.setCategory("test");
        entity.setGeometry("test");
        entity.setFromTime("test");
        entity.setEndTime("test");
        entity.setCreatedTime(new Date());
        entity.setUpdatedTime(new Date());
        entity.setCreatedBy(555L);
        entity.setUpdatedBy(555L);
        // todo:Status ;
        int count = this.service.insert(entity);
        Assert.isTrue(count > 0, "插件数据");
    }


//        /***
//        * 保存
//        */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestEditProduct() {

//        boolean flag = this.service.saveOrUpdateProduct(ProductSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteProduct() {

////boolean flag = this.service.deleteProduct(ProductDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<Product> ProductList = this.service.list(null);
//        if (ProductList != null) {
//            ProductList.forEach(System.out::println);
//        }
//    }
}
