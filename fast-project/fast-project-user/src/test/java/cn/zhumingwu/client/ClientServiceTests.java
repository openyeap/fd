package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ClientService;
import cn.zhumingwu.client.entity.Client;

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
public class ClientServiceTests {

    @Autowired
    ClientService service;


    /***
    * 查询所有
    */
    @Test
    public void TestClientQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertClient() {
        Client entity = new Client();
        entity.setId(555L);
        entity.setUserId(555L);
        entity.setName("test");
        entity.setPhoneNumber("test");
        entity.setEmailAddress("test");
        // todo:ClientType ;
        entity.setClientCode("test");
        entity.setClientSecret("test");
        entity.setAllowScopes("test");
        // todo:GrantType ;
        entity.setTokenLifetime(555L);
        entity.setAccessTokenLifetime(555L);
        entity.setAuthorizationCodeLifetime(555L);
        entity.setRefreshTokenLifetime(555L);
        entity.setClaimPrefix("test");
        entity.setIssueTime(new Date());
        entity.setExpiresIn(555L);
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
//    public void TestEditClient() {

//        boolean flag = this.service.saveOrUpdateClient(ClientSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteClient() {

////boolean flag = this.service.deleteClient(ClientDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<Client> ClientList = this.service.list(null);
//        if (ClientList != null) {
//            ClientList.forEach(System.out::println);
//        }
//    }
}
