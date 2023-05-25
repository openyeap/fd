package cn.zhumingwu.dataswitch.admin.dao;

import lombok.var;
import cn.zhumingwu.dataswitch.admin.entity.JobGroup;
import cn.zhumingwu.dataswitch.admin.repository.JobGroupRepository;
import cn.zhumingwu.dataswitch.admin.service.impl.JobService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobGroupDaoTest {

    @Resource
    private JobGroupRepository jobGroupRepository;
    @Resource
    private JobService jobService;

    @Test
    public void test() {
        List<JobGroup> list = jobGroupRepository.findAll();

        List<JobGroup> list2 = jobService.findByAddressList(0);

        JobGroup group = new JobGroup();
        group.setName("setAppName");
        group.setTitle("setTitle");

        group.setType((byte) 0);
        group.setAddressList("setAddressList");

        var ret = jobGroupRepository.save(group);

        JobGroup group2 = jobGroupRepository.findById(group.getId()).get();
        group2.setName("setAppName2");
        group2.setTitle("setTitle2");

        group2.setType((byte) 2);
        group2.setAddressList("setAddressList2");

        var  ret2 = jobGroupRepository.save(group2);

         jobGroupRepository.deleteById(group.getId());
    }
}
