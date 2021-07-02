package ltd.fdsa.consul.client;

import com.ecwid.consul.UrlParameters;
import com.ecwid.consul.Utils;
import com.ecwid.consul.transport.*;
import com.ecwid.consul.v1.ConsulRawClient;
import com.ecwid.consul.v1.Request;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.consul.properties.ConsulProperties;
import ltd.fdsa.core.event.ServiceDiscoveredEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class MultiConsulRawClient extends ConsulRawClient implements ApplicationListener<ServiceDiscoveredEvent> {
    private final static AtomicInteger next = new AtomicInteger(0);
    private final HttpTransport httpTransport;
    private final String defaultAgentAddress;
    private final ConsulProperties properties;
    private String[] agentAddresses;

    public MultiConsulRawClient(ConsulProperties properties) {
        this.properties = properties;
//        if (this.properties.getTlsConfig() == null) {
        this.httpTransport = new DefaultHttpTransport();
//        } else {
//            this.httpTransport = new DefaultHttpsTransport(this.properties.getTlsConfig());
//        }
        String agentHostLowercase = this.properties.getHost().toLowerCase();
        if (!agentHostLowercase.startsWith("https://") && !agentHostLowercase.startsWith("http://")) {
            this.defaultAgentAddress = Utils.assembleAgentAddress("http://" + this.properties.getHost(), this.properties.getPort(), this.properties.getPath());
        } else {
            this.defaultAgentAddress = Utils.assembleAgentAddress(this.properties.getHost(), this.properties.getPort(), this.properties.getPath());
        }
    }

    @Override
    public void onApplicationEvent(ServiceDiscoveredEvent event) {
        var services = event.getServices().get("consul");
        var urls = services.stream().map(m -> {
            String agentHostLowercase = m.getIp().toLowerCase();
            if (!agentHostLowercase.startsWith("https://") && !agentHostLowercase.startsWith("http://")) {
                return Utils.assembleAgentAddress("http://" + m.getIp(), this.properties.getPort(), this.properties.getPath());
            }
            return Utils.assembleAgentAddress(m.getIp(), this.properties.getPort(), this.properties.getPath());
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

    private String prepareUrl(String url) {
        return url.contains(" ") ? Utils.encodeUrl(url) : url;
    }

    @Retryable(include = {TransportException.class, RetryException.class},
            exclude = IllegalArgumentException.class,
            maxAttempts = 3,
            backoff = @Backoff(0))
    @Override
    public HttpResponse makeDeleteRequest(Request request) {
        String url = this.prepareUrl(this.agentAddress() + request.getEndpoint());
        url = Utils.generateUrl(url, request.getUrlParameters());
        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().setUrl(url).addHeaders(Utils.createTokenMap(request.getToken())).build();
        return this.httpTransport.makeDeleteRequest(httpRequest);
    }


    @Retryable(include = {TransportException.class, RetryException.class},
            exclude = IllegalArgumentException.class,
            maxAttempts = 3,
            backoff = @Backoff(0))
    @Override
    public HttpResponse makeGetRequest(String endpoint, UrlParameters... urlParams) {
        return this.makeGetRequest(endpoint, Arrays.asList(urlParams));
    }

    @Retryable(include = {TransportException.class, RetryException.class},
            exclude = IllegalArgumentException.class,
            maxAttempts = 3,
            backoff = @Backoff(0))
    @Override
    public HttpResponse makePutRequest(Request request) {
        String url = this.prepareUrl(this.agentAddress() + request.getEndpoint());
        url = Utils.generateUrl(url, request.getUrlParameters());
        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().setUrl(url).setBinaryContent(request.getBinaryContent()).addHeaders(Utils.createTokenMap(request.getToken())).build();
        return this.httpTransport.makePutRequest(httpRequest);
    }

    @Retryable(include = {TransportException.class, RetryException.class},
            exclude = IllegalArgumentException.class,
            maxAttempts = 3,
            backoff = @Backoff(0))
    @Override
    public HttpResponse makeGetRequest(Request request) {
        String url = this.prepareUrl(this.agentAddress() + request.getEndpoint());
        url = Utils.generateUrl(url, request.getUrlParameters());
        HttpRequest httpRequest = HttpRequest.Builder.newBuilder().setUrl(url).addHeaders(Utils.createTokenMap(request.getToken())).build();
        return this.httpTransport.makeGetRequest(httpRequest);
    }

    @Retryable(include = {TransportException.class, RetryException.class},
            exclude = IllegalArgumentException.class,
            maxAttempts = 3,
            backoff = @Backoff(0))
    @Override
    public HttpResponse makeGetRequest(String endpoint, List<UrlParameters> urlParams) {
        try {
            String url = this.prepareUrl(this.agentAddress() + endpoint);
            url = Utils.generateUrl(url, urlParams);
            HttpRequest request = HttpRequest.Builder.newBuilder().setUrl(url).build();
            return this.httpTransport.makeGetRequest(request);
        } catch (TransportException exception) {
            return makeGetRequest(endpoint, urlParams);
        }
    }

    @Retryable(include = {TransportException.class, RetryException.class},
            maxAttempts = 3,
            backoff = @Backoff(0))
    @Override
    public HttpResponse makePutRequest(String endpoint, String content, UrlParameters... urlParams) {
        String url = this.prepareUrl(this.agentAddress() + endpoint);
        url = Utils.generateUrl(url, urlParams);
        HttpRequest request = HttpRequest.Builder.newBuilder().setUrl(url).setContent(content).build();
        return this.httpTransport.makePutRequest(request);
    }
}