package cn.zhumingwu.base;

import cn.zhumingwu.base.support.Person;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.config.ProjectAutoConfiguration;
import cn.zhumingwu.base.util.NamingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.annotation.Resource;

@Slf4j
@SpringBootTest
@ContextConfiguration(classes = ProjectAutoConfiguration.class)
public class AOPMethodTests {
    @Autowired
    private Person primaryPerson;

    @Resource(name = "person")
    private Person defaultPerson;

    @Autowired
    private TestClass test;

    @Autowired
    private ApplicationContext applicationContext;

//    @Test
    public void TestPersonExtension() {
        this.test.test();
        this.primaryPerson.say("Jack");
        this.defaultPerson.say("test");
    }

    public static class TestClass {
        public void test() {
            NamingUtils.formatLog(log,"test: {}", "OK");
        }
    }
}
