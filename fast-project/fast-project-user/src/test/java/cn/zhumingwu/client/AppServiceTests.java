package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.AppService;
import cn.zhumingwu.client.entity.App;

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
public class AppServiceTests {

    @Autowired
    AppService service;


    /***
    * 查询所有
    */
    @Test
    public void TestAppQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertApp() {
        App entity = new App();
        entity.setId(555L);
        entity.setName("test");
        entity.setDescription("test");
        entity.setIcon("test");
        entity.setCode("test");
        // todo:AppType ;
        entity.setUrl("test");
        entity.setVendorId(555L);
        entity.setIssueTime(new Date());
        entity.setExpiresIn(555L);
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
//    public void TestEditApp() {

//        boolean flag = this.service.saveOrUpdateApp(AppSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteApp() {

////boolean flag = this.service.deleteApp(AppDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<App> AppList = this.service.list(null);
//        if (AppList != null) {
//            AppList.forEach(System.out::println);
//        }
//    }
}
