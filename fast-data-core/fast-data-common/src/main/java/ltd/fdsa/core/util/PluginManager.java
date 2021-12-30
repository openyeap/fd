package ltd.fdsa.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PluginManager {
    private static volatile PluginManager mgr;
    final ObjectMapper mapper = new ObjectMapper();

    private final Map<String, PluginClassLoader> classLoaderCache = new HashMap<String, PluginClassLoader>();

    public static PluginManager getDefaultInstance() {
        if (mgr == null) {
            synchronized (PluginManager.class) {
                if (mgr == null) mgr = new PluginManager();
            }
        }
        return mgr;
    }


    private Class<?> loadPluginClass(String className) {
        var classLoader = this.classLoaderCache.get(className);
        try {
            return (Class<?>) classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public PluginClassLoader getClassLoader(String className) {
        return this.classLoaderCache.get(className);
    }

    public <T> T getInstance(String className, Class<T> required) {
        Class<?> cls = this.loadPluginClass(className);
        if (required.isAssignableFrom(cls)) {
            try {
                return (T) cls.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("can not newInstance class:" + className, e);
            }
        }
        throw new IllegalArgumentException("class:" + className + " not sub class of " + required);
    }


    public <T> T getInstance(String className, Class<T> required, Object... objects) {
        Class<?> cls = this.loadPluginClass(className);
        try {
            if (required.isAssignableFrom(cls)) {
                for (var constructor : cls.getConstructors()) {
                    return (T) constructor.newInstance(objects);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("class:" + className + " not sub class of " + required);
    }


    public synchronized void scan(String pluginPath) {
        if (Strings.isNullOrEmpty(pluginPath)) {
            throw new IllegalArgumentException("basePath can not be empty!");
        }
        File directory = new File(pluginPath);
        if (!directory.exists()) {
            throw new IllegalArgumentException("basePath not exists:" + pluginPath);
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("basePath must be a directory:" + pluginPath);
        }
        for (var dir : directory.list()) {

            var pluginFile = new File(dir + "/plugins.json");
            if (!pluginFile.exists()) {
                continue;
            }
            PluginClassLoader classLoader = new PluginClassLoader(dir);
            try {
                for (var pluginInfo : mapper.readTree(pluginFile)) {
                    classLoaderCache.put(pluginInfo.get("className").asText(), classLoader);
                }
            } catch (IOException e) {
                log.error("file miss", e);
            }
        }
    }


}
