package cn.zhumingwu.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RSAUtilTest {
    @Test
    public void testFile() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        log.info(path);
    }
}
