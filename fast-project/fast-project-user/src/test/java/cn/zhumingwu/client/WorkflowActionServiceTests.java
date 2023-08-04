package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.WorkflowActionService;
import cn.zhumingwu.client.entity.WorkflowAction;

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
public class WorkflowActionServiceTests {

    @Autowired
    WorkflowActionService service;


    /***
    * 查询所有
    */
    @Test
    public void TestWorkflowActionQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertWorkflowAction() {
        WorkflowAction entity = new WorkflowAction();
        entity.setActionId(555);
        entity.setClientId(555L);
        entity.setWorkflowId(555);
        entity.setParentActionId(555);
        entity.setPreCondition("test");
        entity.setName("test");
        entity.setCode("test");
        entity.setFinishCondition(555);
        // todo:Json ;
        // todo:Json ;
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
//    public void TestEditWorkflowAction() {

//        boolean flag = this.service.saveOrUpdateWorkflowAction(WorkflowActionSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteWorkflowAction() {

////boolean flag = this.service.deleteWorkflowAction(WorkflowActionDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<WorkflowAction> WorkflowActionList = this.service.list(null);
//        if (WorkflowActionList != null) {
//            WorkflowActionList.forEach(System.out::println);
//        }
//    }
}
