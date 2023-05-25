package cn.zhumingwu.dataswitch.admin.dao;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.admin.repository.JobRegistryRepository;
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
    private JobRegistryRepository jobRegistryRepository;

    @Test
    public void test() {
        jobRegistryRepository.deleteById(1);
    }
}
