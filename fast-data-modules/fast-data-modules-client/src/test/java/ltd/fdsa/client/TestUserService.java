package ltd.fdsa.client;
import ltd.fdsa.client.mybatis.generic.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


/**
*
* @ClassName:
* @description:
* @author zhumingwu
* @since 2020-12-09
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {ClientApplication.class})
@Slf4j
public class TestUserService {

    @Autowired
    UserService userService;


    /***
    * 查询所有
    */
    @Test
    public void TestGetUserCount() {
        long count = userService.count();
        Assert.assertTrue("查询到数据", count > 0);
    }

//    /***
//    * 查询分页
//    */
//    @Test
//    public void testGetUserListPage() {
//        UserListReq userListReq = new UserListReq();
//        PageableOut<UserVo> page = userService.getUserListPage(userListReq);
//        Assert.assertNotNull(page.getList());
//    }

//    /***
//    * 保存
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testAddUser() {

//        boolean flag = userService.saveOrUpdateUser(userSaveReq);
//        Assert.assertTrue(flag);
//    }


//        /***
//        * 保存
//        */
//    @Test
//    @Rollback
//    @Transactional
//    public void testEditUser() {

//        boolean flag = userService.saveOrUpdateUser(userSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testDeleteUser() {

////boolean flag = userService.deleteUser(userDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void testSelect() {
//        List<User> userList = userService.list(null);
//        if (userList != null) {
//            userList.forEach(System.out::println);
//        }
//    }

//    /***
//    * 分页查询
//    */
//    @Test
//    public void testSelectForPage() {
//        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
//        IPage page1 = new Page(1, 2);
//        IPage<User> iPage1 = userService.page(page1, query);
//        System.out.println("总页数：" + iPage1.getPages());
//        System.out.println("总记录数：" + iPage1.getTotal());
//        List<User> list1 = iPage1.getRecords();
//        Assert.assertNotNull(list1);
//    }


//    /***
//    * 保存
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testInsert() {
//        User user = new User();

//        userService.save(user);
//    }


//    /***
//    * 批量保存
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testSaveBatch() {
//        User user = new User();
//        List<User> users = Lists.newArrayList(user, user1);
//        userService.saveBatch(users);
//    }

//    /**
//    * 根据ID保存或更新
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testSaveOrUpdate() {
//        User user = new User();

//        userService.saveOrUpdate(user);
//    }


//    /***
//    * 批量保存或更新
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testSaveOrUpdateBatch() {
//        User user = new User();
//        userService.saveOrUpdateBatch(users);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testUpdate() {
//        User user = new User();

//        userService.updateById(user);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testUpdateBatchById() {
//        User user = new User();

//        List<User> users = Lists.newArrayList(user, user1);
//        userService.updateBatchById(users);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testRemove() {
//        User user = new User();

//        userService.remove(queryWrapper);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testRemoveById() {

//    }

}
