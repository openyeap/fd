package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ClientDepartmentService;
import cn.zhumingwu.client.entity.ClientDepartment;

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
public class ClientDepartmentServiceTests {

    @Autowired
    ClientDepartmentService service;


    /***
    * 查询所有
    */
    @Test
    public void TestClientDepartmentQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertClientDepartment() {
        ClientDepartment entity = new ClientDepartment();
        entity.setId(555L);
        entity.setParentId(555L);
        entity.setPath("test");
        entity.setName("test");
        entity.setContent("test");
        entity.setSortNo(555);
        entity.setLeader(555L);
        entity.setPhone("test");
        entity.setEmail("test");
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
//    public void TestEditClientDepartment() {

//        boolean flag = this.service.saveOrUpdateClientDepartment(ClientDepartmentSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteClientDepartment() {

////boolean flag = this.service.deleteClientDepartment(ClientDepartmentDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<ClientDepartment> ClientDepartmentList = this.service.list(null);
//        if (ClientDepartmentList != null) {
//            ClientDepartmentList.forEach(System.out::println);
//        }
//    }
}
