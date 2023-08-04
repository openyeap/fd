package cn.zhumingwu.dataswitch.core;

import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.dataswitch.channel.http.RetryInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;



@SpringBootTest
@ContextConfiguration(classes = {String.class})
@Slf4j
public class RetryInterceptorTest {
    @Autowired
    private StandardEnvironment env;


    @Test
    public void TestHttpRequestRetry() {
        List<String> serverList = new LinkedList<>();
        serverList.add("http://10.168.4.3");
        serverList.add("http://10.158.17.61");
        serverList.add("http://www.blogjava.net");
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .addInterceptor(new RetryInterceptor(serverList.toArray(new String[0])))//添加失败重试及重定向拦截器
//                .retryOnConnectionFailure(true)//允许失败重试
//                .callTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(Duration.ofSeconds(3))
                .build();

        String url = "http://localhost/s";
        Request requestOk = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = okhttpClient.newCall(requestOk).execute();
            log.info("response:{}", response.body().string());
        } catch (IOException e) {
            log.error("ex:", e);
        }
    }
}
