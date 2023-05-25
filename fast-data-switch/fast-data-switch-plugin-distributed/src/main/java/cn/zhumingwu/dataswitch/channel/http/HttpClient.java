package cn.zhumingwu.dataswitch.channel.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Slf4j
public class HttpClient {
    private final List<String> serverList;
    private final OkHttpClient okhttpClient;

    public HttpClient(List<String> serverList) {
        this.serverList = serverList;
        this.okhttpClient = new OkHttpClient.Builder()
                .addInterceptor(new RetryInterceptor(serverList.toArray(new String[0])))//添加失败重试及重定向拦截器
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    public Response get(String path) {
        String url = this.serverList.get(0) + (path.startsWith("/") ? path : "/" + path);
        Request requestOk = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            return this.okhttpClient.newCall(requestOk).execute();
        } catch (IOException e) {
            log.error("ex:", e);
            return null;
        }
    }

    public Response post(String path, RequestBody body) {
        String url = this.serverList.get(0) + (path.startsWith("/") ? path : "/" + path);
        Request requestOk = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            return this.okhttpClient.newCall(requestOk).execute();
        } catch (IOException e) {
            log.error("ex:", e);
            return null;
        }
    }

}
