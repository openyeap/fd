package cn.zhumingwu.cloud.test.rsa;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RSAUtilTest {

    public void setUp() throws Exception {
    }

    @Test
    public void testFile() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        log.info(path);
    }
}
