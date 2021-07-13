package ltd.fdsa.job.admin.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class I18nUtilTest {

    @Test
    public void test() {
        log.info(I18nUtil.getString("admin_name"));
        log.info(I18nUtil.getMultiString("admin_name", "admin_name_full"));
        log.info(I18nUtil.getMultiString());
    }
}
