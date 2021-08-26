package ltd.fdsa.switcher.core.config;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

@Slf4j
public class YamlConfig implements Configuration {
    private final static Yaml yaml = new Yaml();
    final Map<String, Object> config;

    public YamlConfig(String yamlContent) {
        this.config = yaml.load(yamlContent);
    }


    @Override
    public Object get(String path) {
        return null;
    }

    @Override
    public Map<String, String> getPaths() {
        return null;
    }

    @Override
    public Configuration[] getConfigurations(String path) {
        List<Configuration> result = new LinkedList<>();
        var list = Splitter.on(path).splitToList(".");
        for (int i = 0, len = list.size() - 1; i < len; i++) {
            var value = this.config.get(list.get(i));
            if (value instanceof ArrayList) {
                return null;
            }
        }
        return result.toArray(new Configuration[0]);
    }

    @Override
    public Configuration getConfiguration(String path) {

        var list = Splitter.on(path).splitToList(".");
        for (int i = 0, len = list.size() - 1; i < len; i++) {
            var value = this.config.get(list.get(i));
            if (value instanceof ArrayList) {
                return null;
            }
        }
        return this;
    }

    @Override
    public void set(String path, Object value) {

    }
}
