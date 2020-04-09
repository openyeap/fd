package ltd.fdsa.cloud.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ecwid.consul.v1.ConsulClient;
import com.google.api.client.util.Strings;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.service.MinioService;

@RestController
@Slf4j
public class HomeController {
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	RouteDefinitionLocator routeService;

	@Autowired
	MinioService minioService;
	 
	@Autowired
	private ConsulClient consulClient;

	@GetMapping(value = "/")
	public Mono<ResponseEntity<String>> echo() {

		HttpHeaders headers = new HttpHeaders();

		headers.add("Content-Encode", "UTF-8");
		headers.add("Location", "/swagger-ui.html");
		ResponseEntity<String> response = new ResponseEntity<String>("", headers, HttpStatus.TEMPORARY_REDIRECT);

		return Mono.just(response);

	}

	@GetMapping(value = "/csrf")
	public String csrf() {
		return "{}";
	}
 
	@RequestMapping(value = "/routes", method = RequestMethod.GET)
	public Object listRoute() {
		List<Object> list = new ArrayList<Object>();
		routeService.getRouteDefinitions().subscribe(r -> {
			list.add(r);
		});
//		list.add(this.routeService);
		return list;
	}
	@PostMapping("/watch")
	public Object watchChanges(@RequestBody Object body, @RequestHeader HttpHeaders header) {
	 
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

		return list;
	}

	@RequestMapping(value = "/configs", method = RequestMethod.GET)
	public Object getConsulConfig(@RequestParam(required = false) String key) {
		if (Strings.isNullOrEmpty(key)) {
			return this.consulClient.getKVKeysOnly("").getValue();
		}
		return this.consulClient.getKVValue(key).getValue();
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