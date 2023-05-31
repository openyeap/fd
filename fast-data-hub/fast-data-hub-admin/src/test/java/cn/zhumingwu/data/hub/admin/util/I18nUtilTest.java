package cn.zhumingwu.data.hub.admin.util;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.data.hub.core.util.I18nUtil;
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
        log.info(I18nUtil.getInstance("").getString("site_name"));
        log.info(I18nUtil.getInstance("").getMultiString("site_name", "site_name_full"));
        log.info(I18nUtil.getInstance("").getMultiString());
    }
}
