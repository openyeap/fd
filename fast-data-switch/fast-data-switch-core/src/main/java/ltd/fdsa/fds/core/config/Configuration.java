package ltd.fdsa.fds.core.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Configuration {

	String getNecessaryValue(String key);

	String getUnnecessaryValue(String key, String defaultValue);

	Boolean getNecessaryBool(String key);

	/**
	 * 根据用户提供的json path，寻址具体的对象。
	 * <p/>
	 * <br>
	 * <p/>
	 * NOTE: 目前仅支持Map以及List下标寻址, 例如:
	 * <p/>
	 * <br />
	 * <p/>
	 * 对于如下JSON
	 * <p/>
	 * {"a": {"b": {"c": [0,1,2,3]}}}
	 * <p/>
	 * config.get("") 返回整个Map <br>
	 * config.get("a") 返回a下属整个Map <br>
	 * config.get("a.b.c") 返回c对应的数组List <br>
	 * config.get("a.b.c[0]") 返回数字0
	 * 
	 * @return Java表示的JSON对象，如果path不存在或者对象不存在，均返回null。
	 */
	Object get(String path);

	/**
	 * 用户指定部分path，获取Configuration的子集
	 * <p/>
	 * <br>
	 * 如果path获取的路径或者对象不存在，返回null
	 */
	Configuration getConfiguration(String path);

	/**
	 * 根据用户提供的json path，寻址String对象
	 * 
	 * @return String对象，如果path不存在或者String不存在，返回null
	 */
	String getString(String path);

	/**
	 * 根据用户提供的json path，寻址String对象，如果对象不存在，返回默认字符串
	 * 
	 * @return String对象，如果path不存在或者String不存在，返回默认字符串
	 */
	String getString(String path, String defaultValue);

	/**
	 * 根据用户提供的json path，寻址Character对象
	 * 
	 * @return Character对象，如果path不存在或者Character不存在，返回null
	 */
	Character getChar(String path);

	/**
	 * 根据用户提供的json path，寻址Boolean对象，如果对象不存在，返回默认Character对象
	 * 
	 * @return Character对象，如果path不存在或者Character不存在，返回默认Character对象
	 */
	Character getChar(String path, char defaultValue);

	/**
	 * 根据用户提供的json path，寻址Boolean对象
	 * 
	 * @return Boolean对象，如果path值非true,false ，将报错.特别注意：当 path 不存在时，会返回：null.
	 */
	Boolean getBool(String path);

	/**
	 * 根据用户提供的json path，寻址Boolean对象，如果对象不存在，返回默认Boolean对象
	 * 
	 * @return Boolean对象，如果path不存在或者Boolean不存在，返回默认Boolean对象
	 */
	Boolean getBool(String path, boolean defaultValue);

	/**
	 * 根据用户提供的json path，寻址Integer对象
	 * 
	 * @return Integer对象，如果path不存在或者Integer不存在，返回null
	 */
	Integer getInt(String path);

	/**
	 * 根据用户提供的json path，寻址Integer对象，如果对象不存在，返回默认Integer对象
	 * 
	 * @return Integer对象，如果path不存在或者Integer不存在，返回默认Integer对象
	 */
	Integer getInt(String path, int defaultValue);

	/**
	 * 根据用户提供的json path，寻址Long对象
	 * 
	 * @return Long对象，如果path不存在或者Long不存在，返回null
	 */
	Long getLong(String path);

	/**
	 * 根据用户提供的json path，寻址Long对象，如果对象不存在，返回默认Long对象
	 * 
	 * @return Long对象，如果path不存在或者Integer不存在，返回默认Long对象
	 */
	Long getLong(String path, long defaultValue);

	/**
	 * 根据用户提供的json path，寻址Double对象
	 * 
	 * @return Double对象，如果path不存在或者Double不存在，返回null
	 */
	Double getDouble(String path);

	/**
	 * 根据用户提供的json path，寻址Double对象，如果对象不存在，返回默认Double对象
	 * 
	 * @return Double对象，如果path不存在或者Double不存在，返回默认Double对象
	 */
	Double getDouble(String path, double defaultValue);

	/**
	 * 根据用户提供的json path，寻址List对象，如果对象不存在，返回null
	 */
	List<Object> getList(String path);

	/**
	 * 根据用户提供的json path，寻址List对象，如果对象不存在，返回null
	 */
	<T> List<T> getList(String path, Class<T> t);

	/**
	 * 根据用户提供的json path，寻址List对象，如果对象不存在，返回默认List
	 */
	List<Object> getList(String path, List<Object> defaultList);

	/**
	 * 根据用户提供的json path，寻址List对象，如果对象不存在，返回默认List
	 */
	<T> List<T> getList(String path, List<T> defaultList, Class<T> t);

	/**
	 * 根据用户提供的json path，寻址包含Configuration的List，如果对象不存在，返回默认null
	 */
	List<Configuration> getListConfiguration(String path);

	/**
	 * 根据用户提供的json path，寻址Map对象，如果对象不存在，返回null
	 */
	Map<String, Object> getMap(String path);

	/**
	 * 根据用户提供的json path，寻址Map对象，如果对象不存在，返回null;
	 */
	<T> Map<String, T> getMap(String path, Class<T> t);

	/**
	 * 根据用户提供的json path，寻址Map对象，如果对象不存在，返回默认map
	 */
	Map<String, Object> getMap(String path, Map<String, Object> defaultMap);

	/**
	 * 根据用户提供的json path，寻址Map对象，如果对象不存在，返回默认map
	 */
	<T> Map<String, T> getMap(String path, Map<String, T> defaultMap, Class<T> t);

	/**
	 * 根据用户提供的json path，寻址包含Configuration的Map，如果对象不存在，返回默认null
	 */
	Map<String, Configuration> getMapConfiguration(String path);

	/**
	 * 根据用户提供的json path，寻址具体的对象，并转为用户提供的类型
	 * <p/>
	 * <br>
	 * <p/>
	 * NOTE: 目前仅支持Map以及List下标寻址, 例如:
	 * <p/>
	 * <br />
	 * <p/>
	 * 对于如下JSON
	 * <p/>
	 * {"a": {"b": {"c": [0,1,2,3]}}}
	 * <p/>
	 * config.get("") 返回整个Map <br>
	 * config.get("a") 返回a下属整个Map <br>
	 * config.get("a.b.c") 返回c对应的数组List <br>
	 * config.get("a.b.c[0]") 返回数字0
	 * 
	 * @return Java表示的JSON对象，如果转型失败，将抛出异常
	 */
	<T> T get(String path, Class<T> clazz);

	/**
	 * 格式化Configuration输出
	 */
	String beautify();

	/**
	 * 根据用户提供的json path，插入指定对象，并返回之前存在的对象(如果存在)
	 * <p/>
	 * <br>
	 * <p/>
	 * 目前仅支持.以及数组下标寻址, 例如:
	 * <p/>
	 * <br />
	 * <p/>
	 * config.set("a.b.c[3]", object);
	 * <p/>
	 * <br>
	 * 对于插入对象，Configuration不做任何限制，但是请务必保证该对象是简单对象(包括Map<String,
	 * Object>、List<Object>)，不要使用自定义对象，否则后续对于JSON序列化等情况会出现未定义行为。
	 * 
	 * @param path   JSON path对象
	 * @param object 需要插入的对象
	 * @return Java表示的JSON对象
	 */
	Object set(String path, Object object);

	/**
	 * 获取Configuration下所有叶子节点的key
	 * <p/>
	 * <br>
	 * <p/>
	 * 对于<br>
	 * <p/>
	 * {"a": {"b": {"c": [0,1,2,3]}}, "x": "y"}
	 * <p/>
	 * 下属的key包括: a.b.c[0],a.b.c[1],a.b.c[2],a.b.c[3],x
	 */
	Set<String> getKeys();

	/**
	 * 删除path对应的值，如果path不存在，将抛出异常。
	 */
	Object remove(String path);

	/**
	 * 合并其他Configuration，并修改两者冲突的KV配置
	 * 
	 * @param another            合并加入的第三方Configuration
	 * @param updateWhenConflict 当合并双方出现KV冲突时候，选择更新当前KV，或者忽略该KV
	 * @return 返回合并后对象
	 */
	Configuration merge(Configuration another, boolean updateWhenConflict);

	String toString();

	/**
	 * 将Configuration作为JSON输出
	 */
	String toJSON();

	/**
	 * 拷贝当前Configuration，注意，这里使用了深拷贝，避免冲突
	 */
	Configuration clone();

	/**
	 * 按照configuration要求格式的path 比如： a.b.c a.b[2].c
	 * 
	 * @param path
	 */
	void addSecretKeyPath(String path);

	void addSecretKeyPath(Set<String> pathSet);

	void setSecretKeyPathSet(Set<String> keyPathSet);

	boolean isSecretPath(String path);

	Object getInternal();

	Set<String> getSecretKeyPathSet();

}