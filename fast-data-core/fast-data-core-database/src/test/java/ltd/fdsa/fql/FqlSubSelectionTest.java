package ltd.fdsa.fql;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.config.ProjectAutoConfiguration;
import ltd.fdsa.fql.util.FqlUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;


@Slf4j
public class FqlSubSelectionTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void testParser() {
        var util = new FqlUtil(dataSource, new Properties(), new HashMap<>());
        JdbcFqlVisitor ddd =new JdbcFqlVisitor(util);
    }

}
