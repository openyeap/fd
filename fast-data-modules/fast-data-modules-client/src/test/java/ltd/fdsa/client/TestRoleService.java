package ltd.fdsa.client;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.client.mybatis.plus.service.RoleService;
import org.junit.Assert;
import org.junit.Test;
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
public class TestRoleService {

    @Autowired
    RoleService roleService;


    /***
    * 查询所有
    */
    @Test
    public void TestGetRoleCount() {
        long count = roleService.count();
        Assert.assertTrue("查询到数据", count > 0);
    }

//    /***
//    * 查询分页
//    */
//    @Test
//    public void testGetRoleListPage() {
//        RoleListReq roleListReq = new RoleListReq();
//        PageableOut<RoleVo> page = roleService.getRoleListPage(roleListReq);
//        Assert.assertNotNull(page.getList());
//    }

//    /***
//    * 保存
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testAddRole() {

//        boolean flag = roleService.saveOrUpdateRole(roleSaveReq);
//        Assert.assertTrue(flag);
//    }


//        /***
//        * 保存
//        */
//    @Test
//    @Rollback
//    @Transactional
//    public void testEditRole() {

//        boolean flag = roleService.saveOrUpdateRole(roleSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testDeleteRole() {

////boolean flag = roleService.deleteRole(roleDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void testSelect() {
//        List<Role> roleList = roleService.list(null);
//        if (roleList != null) {
//            roleList.forEach(System.out::println);
//        }
//    }

//    /***
//    * 分页查询
//    */
//    @Test
//    public void testSelectForPage() {
//        LambdaQueryWrapper<Role> query = new LambdaQueryWrapper<>();
//        IPage page1 = new Page(1, 2);
//        IPage<Role> iPage1 = roleService.page(page1, query);
//        System.out.println("总页数：" + iPage1.getPages());
//        System.out.println("总记录数：" + iPage1.getTotal());
//        List<Role> list1 = iPage1.getRecords();
//        Assert.assertNotNull(list1);
//    }


//    /***
//    * 保存
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testInsert() {
//        Role role = new Role();

//        roleService.save(role);
//    }


//    /***
//    * 批量保存
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testSaveBatch() {
//        Role role = new Role();
//        List<Role> roles = Lists.newArrayList(role, role1);
//        roleService.saveBatch(roles);
//    }

//    /**
//    * 根据ID保存或更新
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testSaveOrUpdate() {
//        Role role = new Role();

//        roleService.saveOrUpdate(role);
//    }


//    /***
//    * 批量保存或更新
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void testSaveOrUpdateBatch() {
//        Role role = new Role();
//        roleService.saveOrUpdateBatch(roles);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testUpdate() {
//        Role role = new Role();

//        roleService.updateById(role);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testUpdateBatchById() {
//        Role role = new Role();

//        List<Role> roles = Lists.newArrayList(role, role1);
//        roleService.updateBatchById(roles);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testRemove() {
//        Role role = new Role();

//        roleService.remove(queryWrapper);
//    }

//    @Test
//    @Rollback
//    @Transactional
//    public void testRemoveById() {

//    }

}
