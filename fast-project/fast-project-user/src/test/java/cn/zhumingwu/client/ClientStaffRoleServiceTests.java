package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ClientStaffRoleService;
import cn.zhumingwu.client.entity.ClientStaffRole;

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
public class ClientStaffRoleServiceTests {

    @Autowired
    ClientStaffRoleService service;


    /***
    * 查询所有
    */
    @Test
    public void TestClientStaffRoleQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertClientStaffRole() {
        ClientStaffRole entity = new ClientStaffRole();
        entity.setId(555L);
        entity.setStaffId(555L);
        entity.setRoleId(555L);
        entity.setCid(555);
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
//    public void TestEditClientStaffRole() {

//        boolean flag = this.service.saveOrUpdateClientStaffRole(ClientStaffRoleSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteClientStaffRole() {

////boolean flag = this.service.deleteClientStaffRole(ClientStaffRoleDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<ClientStaffRole> ClientStaffRoleList = this.service.list(null);
//        if (ClientStaffRoleList != null) {
//            ClientStaffRoleList.forEach(System.out::println);
//        }
//    }
}
