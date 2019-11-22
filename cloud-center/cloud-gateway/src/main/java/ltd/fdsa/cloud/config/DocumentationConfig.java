package ltd.fdsa.cloud.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.Service;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Slf4j
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
	public static final String API_URI = "/v2/api-docs";

	@Autowired
	ConsulClient consulClient;

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
		resources.add(swaggerResource("default", API_URI, "2.0"));
		try {
			Map<String, Service> list = this.consulClient.getAgentServices().getValue();
			for (String key : list.keySet()) {
				if (!key.contains("consul")) {
					resources.add(swaggerResource(list.get(key).getService(), "/" + list.get(key).getService() + API_URI, "2.0"));
				}
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
		}
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location, String version) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(version);
		return swaggerResource;
	}
}
