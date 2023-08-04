package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.CategoryService;
import cn.zhumingwu.client.entity.Category;

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
public class CategoryServiceTests {

    @Autowired
    CategoryService service;


    /***
    * 查询所有
    */
    @Test
    public void TestCategoryQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertCategory() {
        Category entity = new Category();
        entity.setId(555L);
        entity.setClientId(555L);
        entity.setParentId(555L);
        entity.setPath("test");
        entity.setLevel(555);
        entity.setName("test");
        entity.setIcon("test");
        entity.setContent("test");
        entity.setSortNo(555);
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
//    public void TestEditCategory() {

//        boolean flag = this.service.saveOrUpdateCategory(CategorySaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteCategory() {

////boolean flag = this.service.deleteCategory(CategoryDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<Category> CategoryList = this.service.list(null);
//        if (CategoryList != null) {
//            CategoryList.forEach(System.out::println);
//        }
//    }
}
