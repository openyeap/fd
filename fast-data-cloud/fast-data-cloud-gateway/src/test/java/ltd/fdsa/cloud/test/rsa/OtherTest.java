package ltd.fdsa.cloud.test.rsa;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.aspectj.util.FileUtil;
import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;  
@Slf4j
public class OtherTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		String path1 = "/sdf/sdfsf/sfsdf";
		String[] f = path1.split("/");
		for (String sss : f) {
			log.debug(sss);
		}
	}

}
