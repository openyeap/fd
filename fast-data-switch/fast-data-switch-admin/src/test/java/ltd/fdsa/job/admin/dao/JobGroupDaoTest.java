package ltd.fdsa.job.admin.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import ltd.fdsa.job.admin.core.model.JobGroup;
import ltd.fdsa.job.admin.dao.JobGroupDao;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobGroupDaoTest {

    @Resource
    private JobGroupDao JobGroupDao;

    @Test
    public void test(){
        List<JobGroup> list = JobGroupDao.findAll();

        List<JobGroup> list2 = JobGroupDao.findByAddressType(0);

        JobGroup group = new JobGroup();
        group.setAppName("setAppName");
        group.setTitle("setTitle");
        group.setOrder(1);
        group.setAddressType(0);
        group.setAddressList("setAddressList");

        int ret = JobGroupDao.save(group);

        JobGroup group2 = JobGroupDao.load(group.getId());
        group2.setAppName("setAppName2");
        group2.setTitle("setTitle2");
        group2.setOrder(2);
        group2.setAddressType(2);
        group2.setAddressList("setAddressList2");

        int ret2 = JobGroupDao.update(group2);

        int ret3 = JobGroupDao.remove(group.getId());
    }

}
