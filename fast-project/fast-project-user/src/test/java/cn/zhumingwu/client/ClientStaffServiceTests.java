package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ClientStaffService;
import cn.zhumingwu.client.entity.ClientStaff;

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
public class ClientStaffServiceTests {

    @Autowired
    ClientStaffService service;


    /***
    * 查询所有
    */
    @Test
    public void TestClientStaffQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertClientStaff() {
        ClientStaff entity = new ClientStaff();
        entity.setId(555L);
        entity.setUserId(555L);
        entity.setName("test");
        entity.setDescription("test");
        entity.setSortNo(555);
        entity.setDepartmentId(555L);
        entity.setLeaderId(555L);
        entity.setPhone("test");
        entity.setEmail("test");
        entity.setTitle("test");
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
//    public void TestEditClientStaff() {

//        boolean flag = this.service.saveOrUpdateClientStaff(ClientStaffSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteClientStaff() {

////boolean flag = this.service.deleteClientStaff(ClientStaffDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<ClientStaff> ClientStaffList = this.service.list(null);
//        if (ClientStaffList != null) {
//            ClientStaffList.forEach(System.out::println);
//        }
//    }
}
