package ltd.fdsa.cloud.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.service.DynamicRouteService;

@RestController
@Slf4j
public class HomeController {
	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	DynamicRouteService routeService;

	@Autowired
	ConsulClient consulClient;
	@RequestMapping("/")
	// @PreAuthorize("hasAuthority('BookList')")
	public String home() {
		return "Hello world";
	}

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
