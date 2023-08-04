package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.WorkflowFormService;
import cn.zhumingwu.client.entity.WorkflowForm;

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
public class WorkflowFormServiceTests {

    @Autowired
    WorkflowFormService service;


    /***
    * 查询所有
    */
    @Test
    public void TestWorkflowFormQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertWorkflowForm() {
        WorkflowForm entity = new WorkflowForm();
        entity.setFormId(555);
        entity.setWorkflowId(555);
        entity.setClientId(555L);
        entity.setName("test");
        entity.setCode("test");
        entity.setRemark("test");
        entity.setType("test");
        entity.setSortNo(555);
        entity.setMin(555);
        entity.setMax(555);
        entity.setDefaultValue("test");
        entity.setPattern("test");
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
//    public void TestEditWorkflowForm() {

//        boolean flag = this.service.saveOrUpdateWorkflowForm(WorkflowFormSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteWorkflowForm() {

////boolean flag = this.service.deleteWorkflowForm(WorkflowFormDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<WorkflowForm> WorkflowFormList = this.service.list(null);
//        if (WorkflowFormList != null) {
//            WorkflowFormList.forEach(System.out::println);
//        }
//    }
}
