
package ltd.fdsa.switcher.core.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.assertj.core.util.Strings;

import java.util.Map;

@Slf4j
public class JsonConfig implements Configuration {
    private final JSONObject config;

    public JsonConfig(JSONObject config) {
        this.config = config;
    }

    public JsonConfig(String json) {
        this.config = JSONObject.parseObject(json);
    }

    public JsonConfig(Map<String, Object> map) {
        this.config = new JSONObject(map);
    }

    @Override
    public Configuration[] getConfigurations(String path) {
        var json = this.config.getJSONArray(path);
        if (json == null || json.size() == 0) {
            return new JsonConfig[0];
        }
        JsonConfig[] result = new JsonConfig[json.size()];
        for (int i = 0; i < result.length; i++) {
            var item = json.getJSONObject(i);
            result[i] = new JsonConfig(item);
        }
        return result;
    }

    @Override
    public Configuration getConfiguration(String path) {
        return new JsonConfig(this.config.getJSONObject(path));
    }

    @Override
    public void set(String path, Object value) {
        this.config.put(path,value);
    }

    @Override
    public Object get(String path) {
        return this.config.get(path);
    }

    @Override
    public Map<String, String> getPaths() {
        return null;
    }

    @Override
    public String getString(String path) {
        return this.config.getString(path);
    }

    @Override
    public String getString(String path, String defaultValue) {
        var result = this.getString(path);
        return Strings.isNullOrEmpty(result) ? defaultValue : result;
    }

    @Override
    public Boolean getBool(String path) {
        return this.config.getBoolean(path);
    }

    @Override
    public Boolean getBool(String path, boolean defaultValue) {
        var result = this.getBool(path);
        return result == null ? defaultValue : result;
    }

    @Override
    public Integer getInt(String path) {
        return this.config.getInteger(path);
    }

    @Override
    public Integer getInt(String path, int defaultValue) {
        var result = this.getInt(path);
        return result == null ? defaultValue : result;

    }

    @Override
    public Long getLong(String path) {
        return this.config.getLong(path);
    }

    @Override
    public Long getLong(String path, long defaultValue) {
        var result = this.getLong(path);
        return result == null ? defaultValue : result;
    }

    @Override
    public Double getDouble(String path) {
        return this.config.getDouble(path);
    }

    @Override
    public Double getDouble(String path, double defaultValue) {
        var result = this.getDouble(path);
        return result == null ? defaultValue : result;
    }

}