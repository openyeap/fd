package ltd.fdsa.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.catalog.model.CatalogService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DynamicRouteService implements ApplicationEventPublisherAware, RouteDefinitionRepository {

	@Autowired
	ConsulClient consulClient;
	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}

	public void notifyChanges() {
		this.publisher.publishEvent(new RefreshRoutesEvent(this));
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		List<RouteDefinition> routeDefinitions = new ArrayList<>();
		QueryParams query = QueryParams.Builder.builder().build();
		Map<String, List<String>> services = consulClient.getCatalogServices(query).getValue();

		for (String serviceName : services.keySet()) {
			if (serviceName.equals("consul")) {
				continue;
			}
			RouteDefinition definition = new RouteDefinition();
			definition.setId(serviceName);
			log.debug(serviceName);

			for (CatalogService service : consulClient.getCatalogService(serviceName, query).getValue()) {
				String url = "http://" + service.getServiceAddress() + ":" + service.getServicePort();
				log.debug(url.toString());
				URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
				definition.setUri(uri);
				// 定义第一个断言
				PredicateDefinition predicate = new PredicateDefinition();
				predicate.setName("Path");
				Map<String, String> predicateParams = new HashMap<>(8);
				predicateParams.put("_genkey_0","/"+ service.getServiceName()+"/**");
				predicate.setArgs(predicateParams);
				log.debug(predicateParams.toString());
				definition.setPredicates(Arrays.asList(predicate));
			}
		}
		return Flux.fromIterable(routeDefinitions);
	}

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return null;
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return null;
	}
}