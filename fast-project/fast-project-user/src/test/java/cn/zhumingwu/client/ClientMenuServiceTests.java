package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ClientMenuService;
import cn.zhumingwu.client.entity.ClientMenu;

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
public class ClientMenuServiceTests {

    @Autowired
    ClientMenuService service;


    /***
    * 查询所有
    */
    @Test
    public void TestClientMenuQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertClientMenu() {
        ClientMenu entity = new ClientMenu();
        entity.setId(555L);
        entity.setParentId(555L);
        entity.setPath("test");
        entity.setName("test");
        entity.setContent("test");
        entity.setSortNo(555);
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
//    public void TestEditClientMenu() {

//        boolean flag = this.service.saveOrUpdateClientMenu(ClientMenuSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteClientMenu() {

////boolean flag = this.service.deleteClientMenu(ClientMenuDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<ClientMenu> ClientMenuList = this.service.list(null);
//        if (ClientMenuList != null) {
//            ClientMenuList.forEach(System.out::println);
//        }
//    }
}
