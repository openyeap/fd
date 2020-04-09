package ltd.fdsa.cloud.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import ltd.fdsa.cloud.util.ConsulUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
 
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Slf4j
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
	public static final String API_URI = "/v2/api-docs";
	@Value("${spring.cloud.name:consul}")
	private String name;
	@Autowired
	private DiscoveryClient discoveryClient;

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
		try {

			List<String> map = this.discoveryClient.getServices();
			if (map == null || map.isEmpty()) {
				resources.add(swaggerResource("default", API_URI, "2.0"));
				return resources;
			}
			for (String entry : map) {
				if (this.name.equals(entry)) {
					continue;
				}
				resources.add(swaggerResource(entry, "/" + entry + API_URI + "?group=" + entry, "2.0"));
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
