package cn.zhumingwu.base;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.base.config.ProjectAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ProjectAutoConfiguration.class)
@EnableConfigurationProperties(InnerClass.class)
public class TimeTests {
    long startTime;

    @Autowired InnerClass innerClass;
    @Test
    public void Test_Duration() {
        startTime = System.nanoTime();
        log.info("{}",innerClass.getDelay().getSeconds());
        // parses the given sequence in this Duration du1
        // i.e. 1D:1H:20M:10S in a standard format PnDTnHnMn.S
        Duration du1 = Duration.parse("P10dT10s");

        log.info("{}", du1.getSeconds());
        log.info("{} ns", (System.nanoTime() - startTime) / 1000);
    }


}
