package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.WorkflowService;
import cn.zhumingwu.client.entity.Workflow;

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
public class WorkflowServiceTests {

    @Autowired
    WorkflowService service;


    /***
    * 查询所有
    */
    @Test
    public void TestWorkflowQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertWorkflow() {
        Workflow entity = new Workflow();
        entity.setWorkflowId(555);
        entity.setClientId(555L);
        entity.setName("test");
        entity.setRemark("test");
        // todo:Boolean ;
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
//    public void TestEditWorkflow() {

//        boolean flag = this.service.saveOrUpdateWorkflow(WorkflowSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteWorkflow() {

////boolean flag = this.service.deleteWorkflow(WorkflowDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<Workflow> WorkflowList = this.service.list(null);
//        if (WorkflowList != null) {
//            WorkflowList.forEach(System.out::println);
//        }
//    }
}
