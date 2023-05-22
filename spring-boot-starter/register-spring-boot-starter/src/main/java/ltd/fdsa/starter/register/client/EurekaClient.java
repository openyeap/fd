package ltd.fdsa.starter.register.client;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.event.ServiceDiscoveredEvent;
import ltd.fdsa.starter.register.properties.RegisterProperties;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisTypeAnnos;
import org.springframework.context.ApplicationListener;
import ltd.fdsa.starter.register.model.ServiceInstanceInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class EurekaClient implements ApplicationListener<ServiceDiscoveredEvent> {
    private final static AtomicInteger next = new AtomicInteger(0);
    private final String defaultAgentAddress;
    private final RegisterProperties properties;
    private String[] agentAddresses;

    public EurekaClient(RegisterProperties properties) {
        this.properties = properties;
        String agentHostLowercase = this.properties.getHost().toLowerCase();
        this.defaultAgentAddress = String.format("http://%s:%d/%s", this.properties.getHost(), this.properties.getPort(), this.properties.getPath());
    }

    @Override
    public void onApplicationEvent(ServiceDiscoveredEvent event) {
        var services = event.getServices().get("Eureka");
        var urls = services.stream().map(m -> {
            String agentHostLowercase = m.getIp().toLowerCase();
            return String.format("http://%s:%d/%s", m.getIp(), this.properties.getPort(), this.properties.getPath());
        }).collect(Collectors.toList());
        if (urls.size() <= 0) {
            urls.add(0, this.defaultAgentAddress);
        }
        this.agentAddresses = urls.toArray(new String[0]);
    }

    private String agentAddress() {
        var list = agentAddresses;
        if (list == null || list.length == 0) {
            return this.defaultAgentAddress;
        }
        return list[next.getAndIncrement() % list.length];
    }

    //    Register new application instance	POST /eureka/apps/appID
    @Retryable(include = {RetryException.class}, exclude = IllegalArgumentException.class, maxAttempts = 3, backoff = @Backoff(0))

    public String registerInstance(String serviceName, ServiceInstanceInfo instanceInfo) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(this.agentAddress() + "/eureka/apps/{serviceName}", instanceInfo, String.class, serviceName);
        return responseEntity.getBody();
    }


    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     * De-register application instance	DELETE /eureka/apps/appID/instanceID*/ public void deregisterInstance(String serviceName, String instanceID) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(this.agentAddress() + "/eureka/apps/{serviceName}/{instanceID}", serviceName, instanceID);
    }

    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *  Send application instance heartbeat	PUT /eureka/apps/appID/instanceID
     */ public void heartbeat(String serviceName, String instanceID, Object heartbeatInfo) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(this.agentAddress() + "/eureka/apps/{serviceName}/{instanceID}", heartbeatInfo, serviceName, instanceID);
    }

    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *     Query for all instances	GET /eureka/apps
     */ public List<ServiceInstanceInfo> queryInstances() {
        RestTemplate restTemplate = new RestTemplate();
        var responseEntity = restTemplate.getForEntity(this.agentAddress() + "/eureka/apps", List.class);
        List<ServiceInstanceInfo> result = responseEntity.getBody();
        return result;

    }

    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *     Query for all instances	GET /eureka/apps
     */ public List<ServiceInstanceInfo> queryInstances(String serviceName) {
        RestTemplate restTemplate = new RestTemplate();
        var responseEntity = restTemplate.getForEntity(this.agentAddress() + "/eureka/apps/{serviceName}", List.class, serviceName);
        List<ServiceInstanceInfo> result = responseEntity.getBody();
        return result;
    }


    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *     Query for specific  instance 	GET /eureka/apps/appID/instanceID

     */ public ServiceInstanceInfo queryInstance(String serviceName, String instanceID) {
        RestTemplate restTemplate = new RestTemplate();
        var responseEntity = restTemplate.getForEntity(this.agentAddress() + "/eureka/apps/{serviceName}/{instanceID}", ServiceInstanceInfo.class, serviceName, instanceID);
        return responseEntity.getBody();
    }

    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *     Query for specific  instance 	GET /eureka/instances/instanceID

     */ public ServiceInstanceInfo queryInstance(String instanceID) {
        RestTemplate restTemplate = new RestTemplate();
        var responseEntity = restTemplate.getForEntity(this.agentAddress() + "/eureka/instances/{instanceID}", ServiceInstanceInfo.class, instanceID);
        return responseEntity.getBody();
    }


    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *      Take instance out of service	PUT /eureka/apps/appID/instanceID/status?value=OUT_OF_SERVICE

     */ public void disableInstance(String serviceName, String instanceID) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(this.agentAddress() + "/eureka/apps/{serviceName}/{instanceID}/status?value=OUT_OF_SERVICE", null, serviceName, instanceID);
    }


    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *     Move instance back into service (remove override)	DELETE /eureka/apps/appID/instanceID/status?value=UP

     */ public void enableInstance(String serviceName, String instanceID) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(this.agentAddress() + "/eureka/apps/{serviceName}/{instanceID}/status?value=UP", null, serviceName, instanceID);
    }

    @Retryable(include = {RetryException.class}, maxAttempts = 3, backoff = @Backoff(0))
    /*
     *     Update metadata     PUT /eureka/apps/appID/instanceID/metadata?key=value

     */ public void updateInstanceMetadata(String serviceName, String instanceID, Map<String, String> metadata) {
        RestTemplate restTemplate = new RestTemplate();
        for (var entry : metadata.entrySet()) {
            restTemplate.put(this.agentAddress() + "/eureka/apps/{serviceName}/{instanceID}/metadata?{key}={value}", null, serviceName, instanceID, entry.getKey(), entry.getValue());
        }
    }

}