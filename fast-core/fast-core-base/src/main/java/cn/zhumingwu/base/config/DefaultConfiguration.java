
package cn.zhumingwu.base.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.IOException;
import java.util.*;

@Slf4j
public class DefaultConfiguration implements Configuration {
    static final String DOT = ".";
    static final ObjectMapper JM = new ObjectMapper();
    static final YAMLMapper YM = new YAMLMapper();
    static final JavaPropsMapper PM = new JavaPropsMapper();

    static {
        JM.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    }

    public static Configuration fromProps(String content) {
        try {
            var jsonNode = PM.readTree(content);
            var map = PM.writeValueAsMap(jsonNode);
            return new DefaultConfiguration(map);
        } catch (IOException e) {
            log.error("getPropsConfig failed", e);
        }
        return null;
    }

    public static Configuration fromJson(String content) {
        try {
            var jsonNode = JM.readTree(content);
            var map = PM.writeValueAsMap(jsonNode);
            return new DefaultConfiguration(map);
        } catch (IOException e) {
            log.error("getJsonConfig failed", e);
        }
        return null;
    }

    public static Configuration fromYaml(String content) {
        try {
            var jsonNode = YM.readTree(content);
            var map = PM.writeValueAsMap(jsonNode);
            return new DefaultConfiguration(map);
        } catch (IOException e) {
            log.error("getYamlConfig failed", e);
        }
        return null;
    }

    public static Configuration fromMaps(Map<String, String> config) {
        return new DefaultConfiguration(config);
    }

    private final Map<String, String> config;

    DefaultConfiguration(Map<String, String> map) {
        this.config = map;
    }

    @Override
    public Configuration[] getConfigurations(String path) {
        List<Configuration> result = new LinkedList<>();
        for (var i = 1; i <= 1024; i++) {
            Map<String, String> map = new HashMap<>();
            var key = Strings.isNullOrEmpty(path) ? i + DOT : path + DOT + i + DOT;
            for (var entry : this.config.entrySet()) {
                if (entry.getKey().startsWith(key)) {
                    map.put(entry.getKey().substring(key.length()), entry.getValue());
                }
            }
            if (map.size() > 0) {
                result.add(new DefaultConfiguration(map));
            } else {
                break;
            }
        }
        return result.toArray(new Configuration[0]);
    }

    @Override
    public Configuration getConfiguration(String path) {
        if (Strings.isNullOrEmpty(path)) {
            return this;
        }
        Map<String, String> map = new HashMap<>();
        var key = path + DOT;
        for (var entry : this.config.entrySet()) {
            if (entry.getKey().startsWith(key)) {
                map.put(entry.getKey().substring(key.length()), entry.getValue());
            }
        }
        if (map.size() > 0) {
            return new DefaultConfiguration(map);
        }
        return null;
    }

    @Override
    public void set(String path, String value) {
        this.config.put(path, value);
    }

    @Override
    public Configuration clone() {
        Map<String, String> map = new HashMap<>();
        for (var entry : this.config.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return new DefaultConfiguration(map);
    }

    @Override
    public String get(String path) {
        return this.config.get(path);
    }

    @Override
    public String toString() {
        try {
            return PM.writeValueAsString(this.config);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public String toJson() {
        try {
            var jsonNode = PM.readTree(this.toString());
            return JM.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    public String toYaml() {
        try {
            var jsonNode = PM.readTree(this.toString());
            return YM.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}