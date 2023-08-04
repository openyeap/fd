package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.UserService;
import cn.zhumingwu.client.entity.User;

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
public class UserServiceTests {

    @Autowired
    UserService service;


    /***
    * 查询所有
    */
    @Test
    public void TestUserQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertUser() {
        User entity = new User();
        entity.setId(555L);
        entity.setName("test");
        entity.setUsername("test");
        entity.setPassword("test");
        entity.setNickName("test");
        entity.setMobilePhone("test");
        entity.setEmailAddress("test");
        // todo:UserType ;
        // todo:GenderType ;
        entity.setLanguage("test");
        entity.setTimeZone(555);
        entity.setAvatarUri("test");
        entity.setBirthday(new Date());
        // todo:Boolean ;
        // todo:GradeType ;
        entity.setCountry("test");
        entity.setProvince("test");
        entity.setCity("test");
        entity.setDistrict("test");
        entity.setSalt("test");
        entity.setLoginIp("test");
        entity.setLoginTime(new Date());
        entity.setLoginFailedTimes(555);
        entity.setPasswordExpiresIn(555);
        // todo:Boolean ;
        entity.setLockedTime(new Date());
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
//    public void TestEditUser() {

//        boolean flag = this.service.saveOrUpdateUser(UserSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteUser() {

////boolean flag = this.service.deleteUser(UserDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<User> UserList = this.service.list(null);
//        if (UserList != null) {
//            UserList.forEach(System.out::println);
//        }
//    }
}
