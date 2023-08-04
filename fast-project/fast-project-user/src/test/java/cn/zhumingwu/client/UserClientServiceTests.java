package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.UserClientService;
import cn.zhumingwu.client.entity.UserClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

/**
 * @author zhumingwu
 * @ClassName:
 * @description:
 * @since 2020-12-09
 **/
@SpringBootTest
@ContextConfiguration(classes = {UserApplication.class})
@Slf4j
public class UserClientServiceTests {

    @Autowired
    UserClientService service;


    /***
    * 查询所有
    */
    @Test
    public void TestUserClientQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertUserClient() {
        UserClient entity = new UserClient();
        entity.setId(555L);
        entity.setClientId(555L);
        entity.setUserId(555L);
        int count = this.service.insert(entity);
        Assert.isTrue(count > 0, "插件数据");
    }


//        /***
//        * 保存
//        */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestEditUserClient() {

//        boolean flag = this.service.saveOrUpdateUserClient(UserClientSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteUserClient() {

////boolean flag = this.service.deleteUserClient(UserClientDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<UserClient> UserClientList = this.service.list(null);
//        if (UserClientList != null) {
//            UserClientList.forEach(System.out::println);
//        }
//    }
}
