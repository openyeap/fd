package ltd.fdsa.kafka.stream.util;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author: panhua
 * Create time: 2019/6/2 9:19
 * Description: RestTemplate调用http请求
 */

public class RestTemplateHttpUtil {
    private static String charset = "UTF-8";
    public static final String CONTENT_TYPE = "application/json";

    //获取经过处理的restTemplate
    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new LinkedList<>();
        // String --> UTF-8 --> 解决乱码问题
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter() {
            @Override
            protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
                String readInternal = super.readInternal(clazz, inputMessage);
                return new String(readInternal.getBytes(), getDefaultCharset());
            }
        };
        stringHttpMessageConverter.setDefaultCharset(Charset.forName(charset));
        messageConverters.add(stringHttpMessageConverter);
        // Json --> UTF-8 --> 解决乱码问题
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setDefaultCharset(Charset.forName(charset));
        messageConverters.add(jackson2HttpMessageConverter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    public static String get(String url, Map<String, String> params) {
        String result = null;
        HttpHeaders headers = new HttpHeaders();
        // 设置contentType
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", "application/json");
        //报文
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        //get
        ResponseEntity<String> responseEntity =  getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class,params);
        if (null != responseEntity && null != responseEntity.getBody()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", responseEntity.getStatusCodeValue());
            resultMap.put("result", responseEntity.getBody());
            result = JSON.toJSONString(resultMap);
        }
        return result;
    }
    public static String get(String url) {
        String result = null;
        HttpHeaders headers = new HttpHeaders();
        // 设置contentType
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", CONTENT_TYPE);
        //报文
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        //get
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.GET, requestEntity, String.class);
        if (null != responseEntity && null != responseEntity.getBody()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", responseEntity.getStatusCodeValue());
            resultMap.put("result", responseEntity.getBody());
            result = JSON.toJSONString(resultMap);
        }
        return result;
    }

    public static String delete(String url) {
        String result = null;
        //post
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.DELETE, null, String.class);
        if (null != responseEntity && null != responseEntity.getBody()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", responseEntity.getStatusCodeValue());
            result = JSON.toJSONString(resultMap);
        }
        return result;
    }
    public static String put(String url, String json) {
        String result = null;
        HttpHeaders headers = new HttpHeaders();
        // 设置contentType
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", CONTENT_TYPE);
        //报文
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        //post
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.PUT, requestEntity, String.class);
        if (null != responseEntity && null != responseEntity.getBody()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", responseEntity.getStatusCodeValue());
            result = JSON.toJSONString(resultMap);
        }
        return result;
    }

    public static String post(String url, String json) {
        String result = null;
        HttpHeaders headers = new HttpHeaders();
        // 设置contentType
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", CONTENT_TYPE);
        //报文
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        //post
        ResponseEntity<String> responseEntity = getRestTemplate().exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (null != responseEntity && null != responseEntity.getBody()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", responseEntity.getStatusCodeValue());
            resultMap.put("result", responseEntity.getBody());
            result = JSON.toJSONString(resultMap);
        }
        return result;
    }
}
