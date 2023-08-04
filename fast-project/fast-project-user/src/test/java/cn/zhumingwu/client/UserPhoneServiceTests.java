package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.UserPhoneService;
import cn.zhumingwu.client.entity.UserPhone;

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
public class UserPhoneServiceTests {

    @Autowired
    UserPhoneService service;


    /***
    * 查询所有
    */
    @Test
    public void TestUserPhoneQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertUserPhone() {
        UserPhone entity = new UserPhone();
        entity.setId(555L);
        // todo:PhoneType ;
        entity.setPhoneNumber("test");
        entity.setValidateCode("test");
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
//    public void TestEditUserPhone() {

//        boolean flag = this.service.saveOrUpdateUserPhone(UserPhoneSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteUserPhone() {

////boolean flag = this.service.deleteUserPhone(UserPhoneDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<UserPhone> UserPhoneList = this.service.list(null);
//        if (UserPhoneList != null) {
//            UserPhoneList.forEach(System.out::println);
//        }
//    }
}
