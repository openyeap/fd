package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.UploadFileService;
import cn.zhumingwu.client.entity.UploadFile;

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
public class UploadFileServiceTests {

    @Autowired
    UploadFileService service;


    /***
    * 查询所有
    */
    @Test
    public void TestUploadFileQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertUploadFile() {
        UploadFile entity = new UploadFile();
        entity.setId(555L);
        entity.setName("test");
        entity.setContent("test");
        entity.setUrl("test");
        entity.setSize("test");
        entity.setFromTime(new Date());
        entity.setEndTime(new Date());
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
//    public void TestEditUploadFile() {

//        boolean flag = this.service.saveOrUpdateUploadFile(UploadFileSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteUploadFile() {

////boolean flag = this.service.deleteUploadFile(UploadFileDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<UploadFile> UploadFileList = this.service.list(null);
//        if (UploadFileList != null) {
//            UploadFileList.forEach(System.out::println);
//        }
//    }
}
