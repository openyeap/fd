package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ClientRoleService;
import cn.zhumingwu.client.entity.ClientRole;

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
public class ClientRoleServiceTests {

    @Autowired
    ClientRoleService service;


    /***
    * 查询所有
    */
    @Test
    public void TestClientRoleQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertClientRole() {
        ClientRole entity = new ClientRole();
        entity.setId(555L);
        entity.setCode("test");
        entity.setName("test");
        entity.setDescription("test");
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
//    public void TestEditClientRole() {

//        boolean flag = this.service.saveOrUpdateClientRole(ClientRoleSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteClientRole() {

////boolean flag = this.service.deleteClientRole(ClientRoleDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<ClientRole> ClientRoleList = this.service.list(null);
//        if (ClientRoleList != null) {
//            ClientRoleList.forEach(System.out::println);
//        }
//    }
}
