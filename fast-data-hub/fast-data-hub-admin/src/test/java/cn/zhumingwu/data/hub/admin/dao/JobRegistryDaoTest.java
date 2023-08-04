package cn.zhumingwu.data.hub.admin.dao;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.admin.repository.JobRegistryRepository;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.annotation.Resource;

@Slf4j

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobRegistryDaoTest {

    @Resource
    private JobRegistryRepository jobRegistryRepository;

    @Test
    public void test() {
        jobRegistryRepository.deleteById(1);
    }
}
