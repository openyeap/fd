package ltd.fdsa.cloud.service;

import ltd.fdsa.cloud.util.ConsulUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
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
        Map<String, ArrayList<ConsulUtil.ServiceInfo>> map = ConsulUtil.getInstance().getHealthServices(consulClient);
        if (map == null) {
            return Flux.fromIterable(routeDefinitions);
        }
        for (Map.Entry<String, ArrayList<ConsulUtil.ServiceInfo>> entry : map.entrySet()) {
            if (entry.getValue() == null || entry.getValue().size() == 0) {
                continue;
            }
            RouteDefinition routeDefinition = new RouteDefinition();
            String serviceName = entry.getKey();
            ArrayList<ConsulUtil.ServiceInfo> serviceList = entry.getValue();
            routeDefinition.setId(serviceName);
            for (ConsulUtil.ServiceInfo serviceInfo : serviceList) {
                routeDefinition.setUri(UriComponentsBuilder.fromHttpUrl(serviceInfo.getServiceUrl()).build().toUri());
                //定义断言
                PredicateDefinition predicate = new PredicateDefinition();
                predicate.setName("Path");
                Map<String, String> predicateParams = new HashMap<>(8);
                predicateParams.put("_genkey_0", "/" + serviceName + "/**");
                predicate.setArgs(predicateParams);
                routeDefinition.setPredicates(Arrays.asList(predicate));
                //定义filter
				List<FilterDefinition> filterDefinitions = new ArrayList<>();
				FilterDefinition filterDefinition = new FilterDefinition("SwaggerHeaderFilter");
//				filterDefinitions.add(filterDefinition);
//				filterDefinition = new FilterDefinition("AuthorizeFilter");
                filterDefinitions.add(filterDefinition);
                filterDefinition = new FilterDefinition("StripPrefix=1");
                filterDefinitions.add(filterDefinition);
                routeDefinition.setFilters(filterDefinitions);
            }
            routeDefinitions.add(routeDefinition);
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