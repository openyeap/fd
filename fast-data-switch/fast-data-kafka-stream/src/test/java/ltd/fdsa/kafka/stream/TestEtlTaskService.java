package ltd.fdsa.kafka.stream;

import ltd.fdsa.kafka.stream.Application;
import ltd.fdsa.kafka.stream.service.IEtlTaskService;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Pivotal
 * @ClassName:
 * @description:
 * @since 2021-04-06
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {Application.class})
@Slf4j
public class TestEtlTaskService {

    @Autowired
    IEtlTaskService etlTaskService;


    /***
     * 查询所有
     */
    @Test
    public void TestGetEtlTaskCount() {
        Object object = etlTaskService.findById(1);
        Assert.assertNotNull("查询到数据", object);
    }

}
