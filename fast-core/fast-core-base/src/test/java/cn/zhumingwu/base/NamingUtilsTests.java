package cn.zhumingwu.base;

import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.base.config.ProjectAutoConfiguration;
import cn.zhumingwu.base.util.NamingUtils;
import cn.zhumingwu.base.util.YamlUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ProjectAutoConfiguration.class)
public class NamingUtilsTests {
    static String line = "f_parent_no_leader";
    static String camel = "fParentNoLeader";
    long startTime;

    @Test
    public void Test_underlineToHump() {
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            NamingUtils.underlineToCamel(line);
        }
        NamingUtils.formatLog(log, "underlineToCamel{}: {} ns", NamingUtils.underlineToCamel(line), (System.nanoTime() - startTime));
    }

    @Test
    public void Test_HumpToLine() {
        startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            NamingUtils.camelToUnderline(camel);
        }
        NamingUtils.formatLog(log, "camelToUnderline{}: {} ns", NamingUtils.camelToUnderline(camel), (System.nanoTime() - startTime));
    }


    @Test
    public void Test_Yaml() throws IOException {
        var payload = "spring:\n" +
                "  application:\n" +
                "    name: gateWay\n" +
                "pro:\n" +
                "  name: test";
        for (var item : YamlUtils.load(payload).entrySet()) {
            log.info("{}:{}", item.getKey(), item.getValue());
        }
    }
}
