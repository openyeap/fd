package com.daoshu.cloud.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RequestContext;
import org.springframework.web.server.ServerWebExchange;

import com.daoshu.cloud.filter.ReactiveRequestContextHolder;
import com.daoshu.cloud.service.DynamicRouteService;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.AgentConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;
import com.ecwid.consul.v1.catalog.CatalogConsulClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClientRequest;
import reactor.netty.http.client.HttpClientResponse;
import springfox.documentation.spring.web.json.Json;

@RestController
@Slf4j
public class HomeController {
	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	DynamicRouteService routeService;

	@Autowired
	ConsulClient consulClient;

	@PostMapping("/watch")
	public Object watchChanges(
			@RequestBody Object body,
			@RequestHeader HttpHeaders header)
			throws JsonProcessingException {
		routeService.notifyChanges();
		StringBuffer str = new StringBuffer();
		if (body != null) {
			str.append(body.toString());
			log.info(body.toString());
		}
	
		if (header != null) {
			str.append(header.toString());
			log.info(header.toString());
		}
//		log.info(header.toString());
//		ObjectMapper mapper = new ObjectMapper();
//		  str = mapper.writeValueAsString(s);

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

	@RequestMapping("/reload")
	public String reload() {
		routeService.notifyChanges();
		return "Ok";
	}

	@RequestMapping("/")
	// @PreAuthorize("hasAuthority('BookList')")
	public String home() {
		return "Hello world";
	}

	@RequestMapping("/register")
	public Object registerService() {

		for (int i = 0; i < 10; i++) {
			NewService service = new NewService();
			service.setAddress("http://192.168.100.1" + Integer.toString(i));
			service.setId("id" + Integer.toString(i));
			service.setName("service-name");
			service.setPort(8080 + i);

			// service.setTags(tags);
			consulClient.agentServiceRegister(service);
		}

		Map<String, Service> services = consulClient.getAgentServices().getValue();
		return services;
	}
}
