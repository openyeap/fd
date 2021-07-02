package ltd.fdsa.server.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.URI;


@Slf4j
public class MyClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @SneakyThrows
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var response = execution.execute(request, body);
        if (response.getStatusCode() != HttpStatus.OK) {
            NewRequest newRequest = new NewRequest(request,new URI("http://localhost/"));
            return execution.execute(newRequest, body);
        }
        return response;
    }

    class NewRequest implements HttpRequest {

        private final HttpRequest source;// 原请求request
        private final URI uri;

        public NewRequest(HttpRequest source, URI uri) {
            this.source = source;
            this.uri = uri;
        }

        @Override
        public HttpHeaders getHeaders() {
            return source.getHeaders();
        }

        @Override
        public String getMethodValue() {
            return source.getMethodValue();
        }

        @Override
        public URI getURI() {
            if (uri != null) {
                return uri;
            }
            return source.getURI();
        }
    }
}
