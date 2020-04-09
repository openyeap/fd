package ltd.fdsa.common.plugin;

import java.io.File;

import com.google.common.base.Strings;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j; 

@Slf4j
public class PluginManager {
	private static volatile PluginManager mgr;

	private PluginClassLoader pluginClassLoader;

	private volatile boolean init;

	public static PluginManager getDefaultInstance() {
		if (mgr == null)
			synchronized (PluginManager.class) {
				if (mgr == null)
					mgr = new PluginManager();
			}
		return mgr;
	}

	public <T> T getInstance(Class<T> required) {
		String className = required.getName();
		return getInstance(className, required);
	}

	@SuppressWarnings("unchecked")
	@SneakyThrows
	public <T> T getInstance(String className, Class<T> required) {
		Class<?> cls = null;
		try {
			cls = this.pluginClassLoader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("can not find class:" + className, e);
		}
		if (required.isAssignableFrom(cls))
			try {
				return (T) cls.newInstance();
			} catch (Exception e) {
				throw new IllegalArgumentException("can not newInstance class:" + className, e);
			}
		throw new IllegalArgumentException("class:" + className + " not sub class of " + required);
	}

	public void addExternalJar(String basePath) {
		if (Strings.isNullOrEmpty(basePath))
			throw new IllegalArgumentException("basePath can not be empty!");
		File dir = new File(basePath);
		if (!dir.exists())
			throw new IllegalArgumentException("basePath not exists:" + basePath);
		if (!dir.isDirectory())
			throw new IllegalArgumentException("basePath must be a directory:" + basePath);
		if (!this.init) {
			this.init = true;
			this.pluginClassLoader = doInit(basePath);
		} else {
			this.pluginClassLoader.addToClassLoader(basePath, null, true);
		}
	}

	private synchronized PluginClassLoader doInit(String basePath) {
		PluginClassLoader pluginClassLoader = new PluginClassLoader(basePath);
		return pluginClassLoader;
	}
}
