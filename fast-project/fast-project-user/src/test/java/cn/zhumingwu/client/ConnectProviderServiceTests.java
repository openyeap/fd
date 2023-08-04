package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.ConnectProviderService;
import cn.zhumingwu.client.entity.ConnectProvider;

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
public class ConnectProviderServiceTests {

    @Autowired
    ConnectProviderService service;


    /***
    * 查询所有
    */
    @Test
    public void TestConnectProviderQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertConnectProvider() {
        ConnectProvider entity = new ConnectProvider();
        entity.setId(555L);
        entity.setName("test");
        entity.setDescription("test");
        entity.setLogoUri("test");
        entity.setClientCode("test");
        entity.setClientSecret("test");
        entity.setScope("test");
        entity.setRequestUri("test");
        entity.setRedirectUri("test");
        entity.setResponseType("test");
        entity.setAccessTokenUri("test");
        entity.setGrantType("test");
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
//    public void TestEditConnectProvider() {

//        boolean flag = this.service.saveOrUpdateConnectProvider(ConnectProviderSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteConnectProvider() {

////boolean flag = this.service.deleteConnectProvider(ConnectProviderDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<ConnectProvider> ConnectProviderList = this.service.list(null);
//        if (ConnectProviderList != null) {
//            ConnectProviderList.forEach(System.out::println);
//        }
//    }
}
