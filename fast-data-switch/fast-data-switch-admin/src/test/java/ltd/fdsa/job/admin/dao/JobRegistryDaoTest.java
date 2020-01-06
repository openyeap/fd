package ltd.fdsa.job.admin.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ltd.fdsa.job.admin.core.model.JobRegistry;
import ltd.fdsa.job.admin.dao.JobRegistryDao;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobRegistryDaoTest {

    @Resource
    private JobRegistryDao JobRegistryDao;

    @Test
    public void test(){
        int ret = JobRegistryDao.registryUpdate("g1", "k1", "v1");
        if (ret < 1) {
            ret = JobRegistryDao.registrySave("g1", "k1", "v1");
        }

        List<JobRegistry> list = JobRegistryDao.findAll(1);

        int ret2 = JobRegistryDao.removeDead(Arrays.asList(1));
    }

}
