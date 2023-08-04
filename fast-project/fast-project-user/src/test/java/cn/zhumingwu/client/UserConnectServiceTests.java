package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.UserConnectService;
import cn.zhumingwu.client.entity.UserConnect;

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
public class UserConnectServiceTests {

    @Autowired
    UserConnectService service;


    /***
    * 查询所有
    */
    @Test
    public void TestUserConnectQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertUserConnect() {
        UserConnect entity = new UserConnect();
        entity.setId(555L);
        entity.setUserId(555L);
        entity.setConnectProviderId(555L);
        entity.setAccessToken("test");
        entity.setRefreshToken("test");
        entity.setExpiresIn(555L);
        entity.setRefreshExpiresIn(555L);
        entity.setIssueTime(new Date());
        entity.setOpenId("test");
        entity.setUnionId("test");
        entity.setUserNickName("test");
        entity.setExtensions("test");
        entity.setScopes("test");
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
//    public void TestEditUserConnect() {

//        boolean flag = this.service.saveOrUpdateUserConnect(UserConnectSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteUserConnect() {

////boolean flag = this.service.deleteUserConnect(UserConnectDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<UserConnect> UserConnectList = this.service.list(null);
//        if (UserConnectList != null) {
//            UserConnectList.forEach(System.out::println);
//        }
//    }
}
