package ltd.fdsa.fds.core.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ltd.fdsa.fds.core.exception.FastDataSwitchException;
import ltd.fdsa.fds.util.Utils;

public class EngineConfig implements Configuration {

	private static final String CONFIG_FILE_NAME = "config.json";

	Configuration config;

	protected EngineConfig(String config) {
		this.config = JsonConfiguration.from(config);
	}

	public static EngineConfig create() {

		String path = Utils.getConfigDir() + CONFIG_FILE_NAME;
		try {
			List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName("utf-8"));
			String config = String.join("\n", lines);
			return new EngineConfig(config);
		} catch (IOException e) {

			throw new FastDataSwitchException(e);
		}
	}
 
	@Override
	public String getNecessaryValue(String key) {

		return this.config.getNecessaryValue(key);
	}

	@Override
	public String getUnnecessaryValue(String key, String defaultValue) {

		return this.config.getUnnecessaryValue(key, defaultValue);
	}

	@Override
	public Boolean getNecessaryBool(String key) {

		return this.config.getNecessaryBool(key);
	}

	@Override
	public Object get(String path) {

		return this.config.get(path);
	}

	@Override
	public Configuration getConfiguration(String path) {

		return this.config.getConfiguration(path);
	}

	@Override
	public String getString(String path) {

		return this.config.getString(path);
	}

	@Override
	public String getString(String path, String defaultValue) {

		return this.config.getString(path, defaultValue);
	}

	@Override
	public Character getChar(String path) {

		return this.config.getChar(path);
	}

	@Override
	public Character getChar(String path, char defaultValue) {

		return this.config.getChar(path, defaultValue);
	}

	@Override
	public Boolean getBool(String path) {

		return this.config.getBool(path);
	}

	@Override
	public Boolean getBool(String path, boolean defaultValue) {

		return this.config.getBool(path, defaultValue);
	}

	@Override
	public Integer getInt(String path) {

		return this.config.getInt(path);
	}

	@Override
	public Integer getInt(String path, int defaultValue) {

		return this.config.getInt(path, defaultValue);
	}

	@Override
	public Long getLong(String path) {

		return this.config.getLong(path);
	}

	@Override
	public Long getLong(String path, long defaultValue) {

		return this.config.getLong(path, defaultValue);
	}

	@Override
	public Double getDouble(String path) {

		return this.config.getDouble(path);
	}

	@Override
	public Double getDouble(String path, double defaultValue) {

		return this.config.getDouble(path, defaultValue);
	}

	@Override
	public List<Object> getList(String path) {

		return this.config.getList(path);
	}

	@Override
	public <T> List<T> getList(String path, Class<T> t) {
		return this.config.getList(path, t);
	}

	@Override
	public List<Object> getList(String path, List<Object> defaultList) {

		return this.config.getList(path, defaultList);
	}

	@Override
	public <T> List<T> getList(String path, List<T> defaultList, Class<T> t) {

		return this.config.getList(path, t);
	}

	@Override
	public List<Configuration> getListConfiguration(String path) {

		return this.config.getListConfiguration(path);
	}

	@Override
	public Map<String, Object> getMap(String path) {

		return this.config.getMap(path);
	}

	@Override
	public <T> Map<String, T> getMap(String path, Class<T> t) {

		return this.config.getMap(path, t);
	}

	@Override
	public Map<String, Object> getMap(String path, Map<String, Object> defaultMap) {

		return this.config.getMap(path, defaultMap);
	}

	@Override
	public <T> Map<String, T> getMap(String path, Map<String, T> defaultMap, Class<T> t) {
		return this.config.getMap(path, defaultMap, t);
	}

	@Override
	public Map<String, Configuration> getMapConfiguration(String path) {

		return this.config.getMapConfiguration(path);
	}

	@Override
	public <T> T get(String path, Class<T> clazz) {

		return this.config.get(path, clazz);
	}

	@Override
	public String beautify() {

		return this.config.beautify();
	}

	@Override
	public Object set(String path, Object object) {

		return this.config.set(path, object);
	}

	@Override
	public Set<String> getKeys() {

		return this.config.getKeys();
	}

	@Override
	public Object remove(String path) {

		return this.config.remove(path);
	}

	@Override
	public Configuration merge(Configuration another, boolean updateWhenConflict) {

		return this.config.merge(another, updateWhenConflict);
	}

	@Override
	public String toJSON() {

		return this.config.toJSON();
	}

	@Override
	public Configuration clone() {

		return this.config.clone();
	}

	@Override
	public void addSecretKeyPath(String path) {
		this.config.addSecretKeyPath(path);
	}

	@Override
	public void addSecretKeyPath(Set<String> pathSet) {
		this.config.addSecretKeyPath(pathSet);
	}

	@Override
	public void setSecretKeyPathSet(Set<String> keyPathSet) {
		this.config.setSecretKeyPathSet(keyPathSet);
	}

	@Override
	public boolean isSecretPath(String path) {

		return this.config.isSecretPath(path);
	}

	@Override
	public Object getInternal() {

		return this.config.getInternal();
	}

	@Override
	public Set<String> getSecretKeyPathSet() {

		return this.config.getSecretKeyPathSet();
	}
}
