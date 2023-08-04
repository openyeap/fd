package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.DatasetAttributeService;
import cn.zhumingwu.client.entity.DatasetAttribute;

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
public class DatasetAttributeServiceTests {

    @Autowired
    DatasetAttributeService service;


    /***
    * 查询所有
    */
    @Test
    public void TestDatasetAttributeQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertDatasetAttribute() {
        DatasetAttribute entity = new DatasetAttribute();
        entity.setId("test");
        entity.setDatasetId("test");
        entity.setFileId("test");
        entity.setPath("test");
        entity.setType("test");
        entity.setSize(555L);
        entity.setWidth(555);
        entity.setHeight(555);
        entity.setTags(555L);
        entity.setTypes(555L);
        entity.setScenes(555L);
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
//    public void TestEditDatasetAttribute() {

//        boolean flag = this.service.saveOrUpdateDatasetAttribute(DatasetAttributeSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteDatasetAttribute() {

////boolean flag = this.service.deleteDatasetAttribute(DatasetAttributeDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<DatasetAttribute> DatasetAttributeList = this.service.list(null);
//        if (DatasetAttributeList != null) {
//            DatasetAttributeList.forEach(System.out::println);
//        }
//    }
}
