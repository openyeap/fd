import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class TestCase {

    @Before
    public void before() {

    }

    @Test
    public void testSend() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

        Template fileNameTemplate = new Template("file", " <#list columns as column> ${column}</#list>", configuration);
        StringWriter result = new StringWriter();

        var d = DD.builder().columns(new Column[]{DD.DEFAULT_Column});
        fileNameTemplate.process(d, result);
        var sss = result.toString();

    }

}
