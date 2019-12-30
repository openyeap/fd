package ltd.fdsa.cloud.service;

import java.util.Map;

import ltd.fdsa.cloud.model.ServerInfo;


public interface ConsulService {

	/**
	 * 校验该微服务，是否需要进行证书认证
	 *
	 * @param serverName
	 * @return
	 */
	boolean checkServiceAuth(String serverName);

	/**
	 * Gateway启动时候，初始化KV数据
	 */
	void init();

	/**
	 * watch kv 变化时候调用
	 *
	 * @param map
	 */
	void updateAuthConfig(Map<String, String> map);

	/**
	 * 获取kv对象
	 *
	 * @return
	 */
	Map<String, String> getAuthConfig();

	/**
	 * 获取服务信息
	 *
	 * @return
	 */
	Map<String, ServerInfo> getAllConsulService();

}