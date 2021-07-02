package ltd.fdsa.switcher.core.config;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@Slf4j
public class YamlConfig {
    private final static Yaml yaml = new Yaml();

    public static Configuration YamlConfig(String yamlContent) {
        Map<String, Object> config = yaml.load(yamlContent);
        return new JsonConfig(config);
    }
}
