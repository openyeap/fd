package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.WorkflowInstanceService;
import cn.zhumingwu.client.entity.WorkflowInstance;

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
public class WorkflowInstanceServiceTests {

    @Autowired
    WorkflowInstanceService service;


    /***
    * 查询所有
    */
    @Test
    public void TestWorkflowInstanceQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertWorkflowInstance() {
        WorkflowInstance entity = new WorkflowInstance();
        entity.setInstanceId(555);
        entity.setClientId(555L);
        entity.setWorkflowId(555);
        entity.setActionId(555);
        // todo:Json ;
        entity.setRemark("test");
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
//    public void TestEditWorkflowInstance() {

//        boolean flag = this.service.saveOrUpdateWorkflowInstance(WorkflowInstanceSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteWorkflowInstance() {

////boolean flag = this.service.deleteWorkflowInstance(WorkflowInstanceDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<WorkflowInstance> WorkflowInstanceList = this.service.list(null);
//        if (WorkflowInstanceList != null) {
//            WorkflowInstanceList.forEach(System.out::println);
//        }
//    }
}
