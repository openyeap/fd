package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.PostService;
import cn.zhumingwu.client.entity.Post;

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
public class PostServiceTests {

    @Autowired
    PostService service;


    /***
    * 查询所有
    */
    @Test
    public void TestPostQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertPost() {
        Post entity = new Post();
        entity.setId(555L);
        entity.setClientId(555L);
        entity.setCategoryPath("test");
        entity.setTitle("test");
        // todo:Json ;
        entity.setImage("test");
        entity.setGeometry("test");
        entity.setFromTime(new Date());
        entity.setEndTime(new Date());
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
//    public void TestEditPost() {

//        boolean flag = this.service.saveOrUpdatePost(PostSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeletePost() {

////boolean flag = this.service.deletePost(PostDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<Post> PostList = this.service.list(null);
//        if (PostList != null) {
//            PostList.forEach(System.out::println);
//        }
//    }
}
