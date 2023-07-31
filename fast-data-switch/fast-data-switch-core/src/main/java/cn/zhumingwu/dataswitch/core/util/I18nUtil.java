package cn.zhumingwu.dataswitch.core.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * i18n util
 */
@Slf4j
public class I18nUtil {
    private static final Map<String, I18nUtil> ch = new HashMap<>();
    private final Properties prop;

    public static I18nUtil getInstance(String i18n) {
        if (Strings.isNullOrEmpty(i18n)) {
            i18n = "cn";
        }
        if (ch.containsKey(i18n)) {
            return ch.get(i18n);
        }
        String i18nFile = MessageFormat.format("i18n/message_{0}.properties", i18n);

        // load prop

        try {
            var p = PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource("i18n/message.properties"), "UTF-8"));
            p.putAll(PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource(i18nFile), "UTF-8")));
            ch.put(i18n, new I18nUtil(p));

            return ch.get(i18n);
        } catch (IOException e) {
            log.error("", e);
            return new I18nUtil(new Properties());
        }
    }

    I18nUtil(Properties prop) {
        this.prop = prop;
    }

    /**
     * get val of i18n key
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return this.prop.getProperty(key);
    }


    /**
     * get mult val of i18n mult key, as json
     *
     * @param keys
     * @return
     */
    public String getMultiString(String... keys) {
        Map<String, String> map = new HashMap<String, String>();

        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                map.put(key, prop.getProperty(key));
            }
        } else {
            for (String key : prop.stringPropertyNames()) {
                map.put(key, prop.getProperty(key));
            }
        }

        String json = JacksonUtil.writeValueAsString(map);
        return json;
    }
}
