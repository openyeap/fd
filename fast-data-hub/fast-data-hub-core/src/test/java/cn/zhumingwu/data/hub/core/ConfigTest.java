package cn.zhumingwu.data.hub.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.base.config.DefaultConfiguration;
import cn.zhumingwu.base.config.Configuration;
import cn.zhumingwu.data.hub.core.util.FileUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

@Slf4j
public class ConfigTest {
    private void doPrint(Configuration config) {
        var name = config.get("name");
        log.info("job.name:{}", name);
        var pipelines = config.getConfigurations("pipelines");
        for (var pipeline : pipelines) {
            log.info("pipeline.class:{}", pipeline.get("class"));
        }
        var pipeline = config.getConfiguration("pipelines.1");
        log.info("pipeline.class:{}", pipeline.get("class"));
    }

    @Test
    public void TestCreateConfig() throws JsonProcessingException {
        var list = new User[10];
        for (var i = 0; i < list.length; i++) {
            list[i] = User.builder()
                    .age(Math.random())
                    .name("test" + i)
                    .createTime(new Date())
                    .id(i).types(null).build();
        }
        var obj = User.builder().age(18D).createTime(new Date())
                .id(1).name("adam zhu")
                .types(Arrays.asList("男", "高", "富", "帅"))
                .friends(list).build();
        ObjectMapper objectMapper = new ObjectMapper();
        var content = objectMapper.writeValueAsString(obj);
        DefaultConfiguration config = (DefaultConfiguration) DefaultConfiguration.fromYaml(content);// YamlConfig(content);
        System.out.println("===========props:===========");
        System.out.println(config.toString());
        System.out.println("===========json:===========");
        System.out.println(config.toJson());
        System.out.println("===========yaml:===========");
        System.out.println(config.toYaml());
    }

    @Test
    public void TestYamlConfig() {
        //得到配置
        var url = this.getClass().getClassLoader().getResource("simple_job.yml");
        var content = FileUtils.readFile(url.getFile());
        var config = DefaultConfiguration.fromYaml(content);// YamlConfig(content);
        doPrint(config);
    }

    @Test
    public void TestJsonConfig() {
        //得到配置
        var url = this.getClass().getClassLoader().getResource("simple_job.json");
        var content = FileUtils.readFile(url.getFile());
        var config = DefaultConfiguration.fromJson(content);
        doPrint(config);
    }

    @Test
    public void TestPropertyConfig() {
        //得到配置
        var url = this.getClass().getClassLoader().getResource("simple_job.properties");
        var content = FileUtils.readFile(url.getFile());
        var config = DefaultConfiguration.fromProps(content);
        doPrint(config);
    }

    @Test
    public void TestConfig() {
        //得到配置
        var url = this.getClass().getClassLoader().getResource("plugins.json");
        var content = FileUtils.readFile(url.getFile());
        var config = DefaultConfiguration.fromJson(content);
        for (var item : config.getConfigurations("")) {
            log.info(item.toString());
        }
    }
}
