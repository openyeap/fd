package ltd.fdsa.job.admin.dao;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.job.admin.jpa.service.JobRegistryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobRegistryDaoTest {

    @Resource
    private JobRegistryService JobRegistryDao;

    @Test
    public void test() {
        JobRegistryDao.deleteById(1);
    }
}
