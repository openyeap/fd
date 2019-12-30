package ltd.fdsa.cloud.model;

import ltd.fdsa.cloud.constant.SecretConstant;
import ltd.fdsa.cloud.service.impl.ConsulServiceImpl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import lombok.Data;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.List;

@Data
public   class ServerInfo {
	private String id;
	private String service;
	private List<String> tags;
	private String address;
	private int port;
	private String url;
	private String status;// 0-不可访问，1-可以访问
}