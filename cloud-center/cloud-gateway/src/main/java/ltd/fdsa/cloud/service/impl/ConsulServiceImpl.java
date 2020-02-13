package ltd.fdsa.cloud.service.impl;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Check;
import com.ecwid.consul.v1.agent.model.Service;
import com.ecwid.consul.v1.kv.model.GetValue;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.model.ServerInfo;
import ltd.fdsa.cloud.service.ConsulService;
import ltd.fdsa.cloud.util.Base64Util;
import ltd.fdsa.common.util.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname ConsulServerUtil
 * @Description TODO
 * @Date 2019/12/19 14:53
 * @Author 高进
 */
@org.springframework.stereotype.Service
@Slf4j
public class ConsulServiceImpl implements ConsulService {

	@Autowired
	private ConsulClient consulClient;

	@Value("${spring.daoshu.cert.path:/usr/local/cert}")
	private String certPath;
	@Value("${spring.daoshu.cert.interval:10}")
	private int interval;

	/**
	 * 监听KV数据
	 */
	private static ConcurrentHashMap<String, String> consulAuthConfig = new ConcurrentHashMap<>();

	/**
	 * 监听Services数据
	 */
	private static ConcurrentHashMap<String, ServerInfo> consulServer = new ConcurrentHashMap<>();

	private static Map<String, Long> serviceCheckTime = new HashMap<>();
	private static Map<String, Boolean> serviceCheckStatus = new HashMap<>();

	/**
	 * 校验该微服务，是否需要进行证书认证
	 *
	 * @param serverName
	 * @return
	 */
	@Override
	public boolean checkServiceAuth(String serverName) {
		if (!needCheck(serverName)) {
			return true;
		}
		String snPath = certPath + "/" + serverName + "/" + "sn.pem";
		if (!checkPathExist(snPath)) {
			return false;
		}

		String publicKey = "";

		if (checkPathExist(certPath + "/" + "public.pem")) {
			publicKey = readFile(certPath + "/" + "public.pem");
		} else if (checkPathExist(certPath + "/" + serverName + "/" + "public.pem")) {
			publicKey = readFile(certPath + "/" + serverName + "/" + "public.pem");
		} else {
			return false;
		}

		String sn = readFile(snPath);
		if ("".equals(publicKey) || "".equals(sn)) {
			return false;
		}
		boolean checkFlag = LicenseUtils.verifySerialNumber(publicKey, sn, serverName);
		if (checkFlag) {
			serviceCheckStatus.put(serverName, true);
			serviceCheckTime.put(serverName, System.currentTimeMillis());

		} else {
			serviceCheckStatus.put(serverName, false);
			serviceCheckTime.put(serverName, System.currentTimeMillis());
		}
		return checkFlag;
	}

	/**
	 * server need check
	 * @param serverName
	 * @return true 需要check  false 不需要check
	 */
	private boolean needCheck(String serverName) {

		if (!consulServer.containsKey(serverName)) {
			return false;
		}

		if (!serviceCheckTime.containsKey(serverName)) {
			return true;
		}
		if (!serviceCheckStatus.containsKey(serverName)) {
			return true;
		}
		if (System.currentTimeMillis() - serviceCheckTime.get(serverName) > interval * 60 * 1000) {
			return true;
		}

		return false;
	}

	private boolean checkPathExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	private String readFile(String fileName) {
		String encoding = "UTF-8";
		File file = new File(fileName);
		Long fileLength = file.length();
		byte[] fileContent = new byte[fileLength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(fileContent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(fileContent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gateway启动时候，初始化KV数据
	 */
	@Override
	public void init() {

		consulAuthConfig = new ConcurrentHashMap<>();
		Response<List<GetValue>> response = consulClient.getKVValues("auth/");
		if (response != null && response.getValue() != null) {
			for (GetValue gv : response.getValue()) {
				consulAuthConfig.put(gv.getKey(), gv.getValue() == null ? "" : Base64Util.decode(gv.getValue()));
			}
		}

		consulServer = new ConcurrentHashMap<>();
		Response<Map<String, Service>> agentServices = consulClient.getAgentServices();
		if (agentServices != null && agentServices.getValue() != null) {
			for (Map.Entry<String, Service> entry : agentServices.getValue().entrySet()) {
				if (entry == null) {
					continue;
				}
				// 设置基本信息
				ServerInfo serverInfo = new ServerInfo();
				serverInfo.setId(entry.getValue().getId());
				serverInfo.setService(entry.getValue().getService());
				serverInfo.setTags(entry.getValue().getTags());
				serverInfo.setAddress(entry.getValue().getAddress());
				serverInfo.setPort(entry.getValue().getPort());
				serverInfo.setUrl("http://" + serverInfo.getAddress() + ":" + serverInfo.getPort());
				consulServer.put(serverInfo.getService(), serverInfo);
			}
		}
		Response<Map<String, Check>> agentChecks = consulClient.getAgentChecks();
		if (agentChecks != null && agentChecks.getValue() != null) {
			for (Map.Entry<String, Check> entry : agentChecks.getValue().entrySet()) {
				if (entry == null) {
					continue;
				}
				if (entry.getValue().getStatus() == Check.CheckStatus.PASSING) {
					consulServer.get(entry.getValue().getServiceName()).setStatus("1");
				} else {
					consulServer.get(entry.getValue().getServiceName()).setStatus("0");
				}
			}
		}
	}

	/**
	 * watch kv 变化时候调用
	 *
	 * @param map
	 */
	@Override
	public void updateAuthConfig(Map<String, String> map) {
		consulAuthConfig.putAll(map);
	}

	/**
	 * 获取kv对象
	 *
	 * @return
	 */
	@Override
	public Map<String, String> getAuthConfig() {

		return consulAuthConfig;
	}

	/**
	 * 获取服务信息
	 *
	 * @return
	 */
	@Override
	public Map<String, ServerInfo> getAllConsulService() {
		return consulServer;
	}
}
