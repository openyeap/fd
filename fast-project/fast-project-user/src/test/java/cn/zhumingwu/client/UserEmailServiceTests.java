package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.UserEmailService;
import cn.zhumingwu.client.entity.UserEmail;

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
public class UserEmailServiceTests {

    @Autowired
    UserEmailService service;


    /***
    * 查询所有
    */
    @Test
    public void TestUserEmailQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertUserEmail() {
        UserEmail entity = new UserEmail();
        entity.setId(555L);
        entity.setEmailAddress("test");
        entity.setValidateCode("test");
        entity.setValidateTime(new Date());
        entity.setConfirmedTime(new Date());
        entity.setExpiresIn(555L);
        entity.setUserId(555L);
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
//    public void TestEditUserEmail() {

//        boolean flag = this.service.saveOrUpdateUserEmail(UserEmailSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteUserEmail() {

////boolean flag = this.service.deleteUserEmail(UserEmailDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<UserEmail> UserEmailList = this.service.list(null);
//        if (UserEmailList != null) {
//            UserEmailList.forEach(System.out::println);
//        }
//    }
}
