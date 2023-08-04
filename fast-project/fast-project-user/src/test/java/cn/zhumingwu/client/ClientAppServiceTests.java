package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ClientAppService;
import cn.zhumingwu.client.entity.ClientApp;

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
public class ClientAppServiceTests {

    @Autowired
    ClientAppService service;


    /***
    * 查询所有
    */
    @Test
    public void TestClientAppQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertClientApp() {
        ClientApp entity = new ClientApp();
        entity.setId(555L);
        entity.setAppId(555L);
        entity.setAppCode("test");
        entity.setAppSecret("test");
        entity.setName("test");
        entity.setDescription("test");
        entity.setTokenLifetime(555L);
        entity.setAccessTokenLifetime(555L);
        entity.setAuthorizationCodeLifetime(555L);
        entity.setRefreshTokenLifetime(555L);
        entity.setClaimPrefix("test");
        entity.setIssueTime(new Date());
        entity.setExpiresIn(555L);
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
//    public void TestEditClientApp() {

//        boolean flag = this.service.saveOrUpdateClientApp(ClientAppSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteClientApp() {

////boolean flag = this.service.deleteClientApp(ClientAppDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<ClientApp> ClientAppList = this.service.list(null);
//        if (ClientAppList != null) {
//            ClientAppList.forEach(System.out::println);
//        }
//    }
}
