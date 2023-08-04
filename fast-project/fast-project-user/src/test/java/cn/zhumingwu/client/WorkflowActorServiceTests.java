package cn.zhumingwu.client;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.client.service.WorkflowActorService;
import cn.zhumingwu.client.entity.WorkflowActor;

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
public class WorkflowActorServiceTests {

    @Autowired
    WorkflowActorService service;


    /***
    * 查询所有
    */
    @Test
    public void TestWorkflowActorQuery() {
        long count = this.service.findAll("").size();
        Assert.isTrue(count > 0, "查询到数据");
    }
 
    @Test
    public void TestInsertWorkflowActor() {
        WorkflowActor entity = new WorkflowActor();
        entity.setWorkflowActorId(555);
        entity.setClientId(555L);
        entity.setWorkflowInstanceId(555);
        entity.setActionId(555);
        entity.setActorId(555);
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
//    public void TestEditWorkflowActor() {

//        boolean flag = this.service.saveOrUpdateWorkflowActor(WorkflowActorSaveReq);
//        Assert.assertTrue(flag);
//    }


//    /***
//    * 删除
//    */
//    @Test
//    @Rollback
//    @Transactional
//    public void TestDeleteWorkflowActor() {

////boolean flag = this.service.deleteWorkflowActor(WorkflowActorDelReq);

//        Assert.assertTrue(flag);
//    }



//    /***
//    * 查询所有
//    */
//    @Test
//    public void TestSelect() {
//        List<WorkflowActor> WorkflowActorList = this.service.list(null);
//        if (WorkflowActorList != null) {
//            WorkflowActorList.forEach(System.out::println);
//        }
//    }
}
