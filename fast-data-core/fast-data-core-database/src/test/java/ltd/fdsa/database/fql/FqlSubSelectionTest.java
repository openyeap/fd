package ltd.fdsa.database.fql;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.database.properties.JdbcApiProperties;
import ltd.fdsa.database.service.JdbcApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class FqlSubSelectionTest {

    @Autowired
    DataSource dataSource;
    @Autowired
    JdbcApiProperties properties;

    @Test
    public void testParser() {
        var util = new JdbcApiService(this.properties, this.dataSource);
        JdbcFqlVisitor ddd = new JdbcFqlVisitor(util);
    }

}
