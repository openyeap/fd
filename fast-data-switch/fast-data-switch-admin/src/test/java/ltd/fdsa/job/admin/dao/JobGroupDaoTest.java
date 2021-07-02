package ltd.fdsa.job.admin.dao;

import lombok.var;
import ltd.fdsa.job.admin.jpa.entity.JobGroup;
import ltd.fdsa.job.admin.jpa.service.JobGroupService;
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
    private JobGroupService JobGroupDao;

    @Test
    public void test() {
        List<JobGroup> list = JobGroupDao.findAll();

        List<JobGroup> list2 = JobGroupDao.findByAddressType(0);

        JobGroup group = new JobGroup();
        group.setName("setAppName");
        group.setTitle("setTitle");

        group.setType((byte) 0);
        group.setAddressList("setAddressList");

        var ret = JobGroupDao.update(group);

        JobGroup group2 = JobGroupDao.findById(group.getId()).get();
        group2.setName("setAppName2");
        group2.setTitle("setTitle2");

        group2.setType((byte) 2);
        group2.setAddressList("setAddressList2");

        var  ret2 = JobGroupDao.update(group2);

         JobGroupDao.deleteById(group.getId());
    }
}
