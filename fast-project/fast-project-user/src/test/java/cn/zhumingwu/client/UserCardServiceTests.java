package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.UserCardService;
import cn.zhumingwu.client.entity.UserCard;

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
public class UserCardServiceTests {

    @Autowired
    UserCardService service;


    /***
    * 查询所有
    */
    @Test
    public void TestUserCardQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertUserCard() {
        UserCard entity = new UserCard();
        entity.setId(555L);
        entity.setCode("test");
        // todo:GradeType ;
        entity.setName("test");
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
//    public void TestEditUserCard() {

//        boolean flag = this.service.saveOrUpdateUserCard(UserCardSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteUserCard() {

////boolean flag = this.service.deleteUserCard(UserCardDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<UserCard> UserCardList = this.service.list(null);
//        if (UserCardList != null) {
//            UserCardList.forEach(System.out::println);
//        }
//    }
}
