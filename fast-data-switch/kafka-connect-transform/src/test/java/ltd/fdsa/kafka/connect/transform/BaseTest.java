package ltd.fdsa.kafka.connect.transform;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseTest {


    @Before
    public void before() {

    }

    @Test
    public void testSend() {
        var root = this.getClass().getClassLoader().getResource("./");
        if (root == null || !root.getProtocol().equals("file")) {
            return;
        }
        try {
            for (var file : find(new File(root.toURI()))) {
                log.info(file.getAbsolutePath());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private List<File> find(File dirFile) {

        List<File> result = new ArrayList<File>();

        if (!dirFile.exists()) {
            return result;
        }
        if (dirFile.isFile()) {
            result.add(dirFile);
            return result;
        }
        for (File file : dirFile.listFiles()) {
            result.addAll(find(file));
        }
        return result;
    }
}