package ltd.fdsa.core;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.core.config.ProjectAutoConfiguration;
import ltd.fdsa.core.support.Person;
import ltd.fdsa.core.util.NamingUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
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

    @Test
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
