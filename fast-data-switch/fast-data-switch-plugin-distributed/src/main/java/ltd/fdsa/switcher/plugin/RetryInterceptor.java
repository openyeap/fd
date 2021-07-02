package ltd.fdsa.switcher.plugin;


import lombok.extern.slf4j.Slf4j;
import lombok.var;
import okhttp3.*;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class RetryInterceptor implements Interceptor {

    private final static AtomicInteger next = new AtomicInteger(0);

    private final int retryTimes;
    private final String[] servers;

    public RetryInterceptor(String[] servers) {
        this.servers = servers;
        this.retryTimes = this.servers.length;
    }

    public RetryInterceptor(String[] servers, int retryTimes) {
        this.servers = servers;
        if (retryTimes <= 0) {
            this.retryTimes = this.servers.length;
        } else {
            this.retryTimes = retryTimes;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        int count = 0;
        List errors = new LinkedList();
        try {
            return chain.proceed(chain.request());
        } catch (IOException e) {
            log.debug("times:{0}", count, e);
            errors.add(MessageFormat.format("times:{0} , url:{1}, code:{2}, message:{3}", count, chain.request().url(), 0, e.getLocalizedMessage()));
        }
        String path = chain.request().url().encodedPath();

        do {
            count++;
            var url = this.servers[next.getAndIncrement() % this.servers.length] + path;
            try {
                Request newRequest = chain.request().newBuilder().url(url).build();
                var response = chain.proceed(newRequest);
                MediaType mediaType = response.body().contentType();
                String content = response.body().string();
                return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
                //errors.add(MessageFormat.format("times:{0} , url:{1}, code:{2}, message:{3}", count, url, response.code(), response.message()));
            } catch (IOException e) {
                log.debug("times:{0}", count, e);
                errors.add(MessageFormat.format("times:{0} , url:{1}, code:{2}, message:{3}", count, url, 0, e.getLocalizedMessage()));
            }
        } while (count < this.retryTimes);
        if (errors.size() > 0) {
            var message = errors.stream().map(m -> m.toString()).collect(Collectors.joining(System.lineSeparator())).toString();
            throw new IOException(message);
        } else {
            throw new IOException();
        }
    }
}
