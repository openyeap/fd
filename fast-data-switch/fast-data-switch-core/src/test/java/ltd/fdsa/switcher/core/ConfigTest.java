package ltd.fdsa.switcher.core;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.util.FileUtils;
import ltd.fdsa.core.util.YamlUtils;
import ltd.fdsa.switcher.core.config.Configuration;
import ltd.fdsa.switcher.core.config.EnvironmentConfig;
import ltd.fdsa.switcher.core.config.YamlConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


/**
 * @ClassName:
 * @description:
 * @since 2020-10-28
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {String.class})
@Slf4j
public class ConfigTest {
    @Autowired
    private StandardEnvironment env;

    @Test
    public void TestConfig() {
        //得到配置
        var url = this.getClass().getClassLoader().getResource("application.yml");
        var content = FileUtils.readFile(url.getFile());

        var config = new YamlConfig(content);// YamlConfig(content);
        var slaves = config.getString("spring.datasource.slaves");
        log.info("{}", slaves);
        var app = config.getString("spring.application.name");
        log.info("{}", app);

        var list = config.getConfigurations("spring.datasource.slaves");

        log.info("{}", list);

        var nu = config.getConfiguration("spring.datasource.slaves");
        log.info("{}", nu);
        var datasource = config.getConfiguration("spring.datasource");
        log.info("{}", datasource);

        for (var item : this.env.getPropertySources()) {
            try {

                log.info("{}", item.getClass());
            } catch (Exception ex) {
                log.error("ss", ex);
            }
        }
    }


    @Test
    public void TestProperty() {
        //得到配置
        var url = this.getClass().getClassLoader().getResource("application.properties");
        var content = FileUtils.readFile(url.getFile());
        Properties config = new Properties();
        try {
            InputStream inputStream = url.openStream();
            config.load(inputStream);
        } catch (IOException e) {
        }


        var slaves = config.getProperty("spring.datasource.slaves");
        log.info("{}", slaves);
        var app = config.getProperty("spring.application.name");
        log.info("{}", app);
        var application = config.getProperty("spring.application");
        log.info("{}", application);

        var list = config.getProperty("spring.datasource.slaves");

        log.info("{}", list);

        var nu = config.getProperty("spring.datasource.slaves");
        log.info("{}", nu);
        var datasource = config.getProperty("spring.datasource");
        log.info("{}", datasource);
    }


}
