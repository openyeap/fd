package ltd.fdsa.switcher.core.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.assertj.core.util.Strings;
import org.springframework.core.env.Environment;

import java.util.Map;

@AllArgsConstructor
@Slf4j
public class EnvironmentConfig implements Configuration {
    private final Environment config;

    @Override
    public Object get(String path) {
        return this.config.getProperty(path);
    }

    @Override
    public Map<String, String> getPaths() {
        return null;
    }

    @Override
    public Boolean getBool(String path) {
        var result =this.getString(path);
        if(Strings.isNullOrEmpty(result))
        {
            return false;
        }
        return  Boolean.valueOf(result);
    }

    @Override
    public Boolean getBool(String path, boolean defaultValue) {
        var result =this.getString(path);
        if (Strings.isNullOrEmpty(result)) {
            return defaultValue;
        }
        return Boolean.valueOf(result);
    }

    @Override
    public Integer getInt(String path) {
        var result = this.getString(path);
        if (Strings.isNullOrEmpty(result)) {
            return 0;
        }
        return Integer.valueOf(result);
    }

    @Override
    public Integer getInt(String path, int defaultValue) {
        var result = this.getString(path);
        if (Strings.isNullOrEmpty(result)) {
            return defaultValue;
        }
        return Integer.valueOf(result);
    }

    @Override
    public Long getLong(String path) {
        var result = this.getString(path);
        if (Strings.isNullOrEmpty(result)) {
            return 0L;
        }
        return Long.valueOf(result);
    }

    @Override
    public Long getLong(String path, long defaultValue) {
        var result = this.getString(path);
        if (Strings.isNullOrEmpty(result)) {
            return 0L;
        }
        return Long.valueOf(result);
    }

    @Override
    public Double getDouble(String path) {
        var result = this.getString(path);
        if (Strings.isNullOrEmpty(result)) {
            return 0D;
        }
        return Double.valueOf(result);

    }

    @Override
    public Double getDouble(String path, double defaultValue) {
        var result = this.getString(path);
        if (Strings.isNullOrEmpty(result)) {
            return defaultValue;
        }
        return Double.valueOf(result);
    }

    @Override
    public Configuration[] getConfigurations(String path) {
        return null;
    }

    @Override
    public Configuration getConfiguration(String path) {
        return null;
    }

    @Override
    public void set(String path, Object value) {

    }

}
