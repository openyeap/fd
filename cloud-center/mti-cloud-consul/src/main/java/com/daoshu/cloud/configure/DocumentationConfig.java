package com.daoshu.cloud.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
	public static final String API_URI = "/v2/api-docs";
//	private final RouteLocator routeLocator;
//	private final DocumentationCache documentationCache;
	private final DiscoveryClient discoveryClient;

	public DocumentationConfig(// RouteLocator routeLocator,
//			DocumentationCache documentationCache,
			DiscoveryClient discoveryClient) {
//		this.routeLocator = routeLocator;
//		this.documentationCache = documentationCache;
		this.discoveryClient = discoveryClient;
	}

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<SwaggerResource>();

		List<String> list = this.discoveryClient.getServices();

		list.forEach(s -> {
			if (!s.contains("consul")) {
				resources.add(swaggerResource(s, "/" + s + API_URI, "2.0"));
			}
		});
		// load routers from documentationCache
//		Map<String, Documentation> documentations = documentationCache.all();
//		for (Map.Entry<String, Documentation> entry : documentations.entrySet()) {
//			String swaggerGroup = entry.getKey();
//			SwaggerResource swaggerResource = new SwaggerResource();
//			swaggerResource.setName(swaggerGroup);
//			String base = Optional.of("/v2/api-docs").get();
//			if (!Docket.DEFAULT_GROUP_NAME.equals(swaggerGroup)) {
//				base += "?group=" + swaggerGroup;
//			}
//			swaggerResource.setLocation(base);
//			swaggerResource.setSwaggerVersion("2.0");
//			resources.add(swaggerResource);
//		}
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
