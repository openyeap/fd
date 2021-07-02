package ltd.fdsa.cloud.test.rsa;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class RSAUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testFile() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        log.info(path);
    }
}
