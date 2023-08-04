package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.DatasetService;
import cn.zhumingwu.client.entity.Dataset;

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
public class DatasetServiceTests {

    @Autowired
    DatasetService service;


    /***
    * 查询所有
    */
    @Test
    public void TestDatasetQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertDataset() {
        Dataset entity = new Dataset();
        entity.setId("test");
        entity.setName("test");
        entity.setDescription("test");
        entity.setTags("test");
        entity.setTypes("test");
        entity.setScenes("test");
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
//    public void TestEditDataset() {

//        boolean flag = this.service.saveOrUpdateDataset(DatasetSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteDataset() {

////boolean flag = this.service.deleteDataset(DatasetDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<Dataset> DatasetList = this.service.list(null);
//        if (DatasetList != null) {
//            DatasetList.forEach(System.out::println);
//        }
//    }
}
