package ltd.fdsa.cloud.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ltd.fdsa.cloud.util.Base64Util;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.Service;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.model.ServerInfo;
import ltd.fdsa.cloud.service.ConsulService;
import ltd.fdsa.cloud.service.DynamicRouteService;
import ltd.fdsa.cloud.service.MinioService;

@RestController
@Slf4j
public class HomeController {
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	DynamicRouteService routeService;

	@Autowired
	MinioService minioService;
	@Autowired
	ConsulService consulService;

	@Autowired
	ConsulClient consulClient;

	@PostMapping("/watch")
	public Object watchChanges(@RequestBody Object body, @RequestHeader HttpHeaders header) {
		routeService.notifyChanges();
		StringBuffer str = new StringBuffer();
		if (body != null) {
			str.append(body.toString());
		}

		if (header != null) {
			str.append(header.toString());
		}
		return str;
	}

	@RequestMapping("/services")
	public Object serviceUrl() {
		List<Object> list = new ArrayList<Object>();
		for (String item : this.discoveryClient.getServices()) {
			list.addAll(this.discoveryClient.getInstances(item));
		}

		Map<String, Service> services = consulClient.getAgentServices().getValue();
		for (String key : services.keySet()) {
			list.add(key);
			list.add(services.get(key));
		}
		return list;
	}

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public Map<String, String> initConsulKV() {
		consulService.init();
		return consulService.getAuthConfig();
	}

	@RequestMapping(value = "/authWatch", method = RequestMethod.POST)
	public Map<String, String> consulKVChanges(@RequestBody List<HashMap<String, String>> list) {
		Map<String, String> map = new HashMap<>();
		for (HashMap<String, String> info : list) {
			map.put(info.get("Key"), info.get("Value") == null ? "" : Base64Util.decode(info.get("Value")));
		}
		consulService.updateAuthConfig(map);
		return consulService.getAuthConfig();
	}

	@RequestMapping(value = "/getKV", method = RequestMethod.GET)
	public Map<String, String> getConsulKVs() {
		return consulService.getAuthConfig();
	}

	@RequestMapping(value = "/serviceWatch", method = RequestMethod.POST)
	public Map<String, ServerInfo> consulServiceChanges(@RequestBody Object obj) {
		consulService.init();
		return consulService.getAllConsulService();
	}

	@RequestMapping(value = "/getService", method = RequestMethod.GET)
	public Map<String, ServerInfo> getConsulServices() {
		return consulService.getAllConsulService();
	}

	@RequestMapping(value = "/file", method = RequestMethod.POST)
	public void upload(@RequestParam("file") MultipartFile data, String bucketName, String path) throws IOException {
		String objectName = data.getOriginalFilename();
		String contentType = data.getContentType();
		InputStream stream = data.getInputStream();
		long size = data.getSize();
		minioService.putObject(bucketName, objectName, stream, size, contentType);
	}

	@RequestMapping(value = "/file", method = RequestMethod.GET)
	public Mono<ResponseEntity<byte[]>> file(String bucketName, String path) {
		String objectName = path;
		try (InputStream inputStream = this.minioService.getObject(bucketName, objectName)) {
			byte[] body = new byte[inputStream.available()];
			inputStream.read(body);
			String fileName = java.net.URLEncoder.encode(objectName, "UTF-8");
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment;filename=" + fileName);
			headers.add("Content-Type", "application/force-download");
			headers.add("Content-Encode", "UTF-8");
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
			return Mono.just(response);
		} catch (Exception e) {
			log.error("文件读取异常", e);
			return Mono.just(new ResponseEntity<byte[]>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR));
		}
	}

}