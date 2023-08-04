package cn.zhumingwu.data.hub.admin.dao;


import cn.zhumingwu.dataswitch.admin.service.impl.JobService;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.annotation.Resource;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobGroupDaoTest {

    @Resource
    private JobGroupRepository jobGroupRepository;
    @Resource
    private JobService jobService;

    @Test
    public void test() {
        List<JobExecutor> list = jobGroupRepository.findAll();

        List<JobExecutor> list2 = jobService.findByAddressList(0);

        JobExecutor group = new JobExecutor();
        group.setName("setAppName");
        group.setInstanceId("setTitle");

        group.setType((byte) 0);
        group.setHandlerList("setAddressList");

        var ret = jobGroupRepository.save(group);

        JobExecutor group2 = jobGroupRepository.findById(group.getId()).get();
        group2.setName("setAppName2");
        group2.setInstanceId("setTitle2");

        group2.setType((byte) 2);
        group2.setHandlerList("setAddressList2");

        var  ret2 = jobGroupRepository.save(group2);

         jobGroupRepository.deleteById(group.getId());
    }
}
