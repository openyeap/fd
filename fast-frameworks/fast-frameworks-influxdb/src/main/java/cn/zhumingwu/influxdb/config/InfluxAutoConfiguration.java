package cn.zhumingwu.influxdb.config;

import cn.zhumingwu.influxdb.properties.InfluxProperties;
import com.google.common.base.Strings;
import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;
import lombok.var;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({InfluxProperties.class})
public class InfluxAutoConfiguration {
//    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;
//
//    public InfluxAutoConfiguration(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
//        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
//    }
//
//    @PostConstruct
//    private void addArgumentResolvers() {
//        List<HandlerMethodArgumentResolver> argumentResolvers = new LinkedList<>();
//        argumentResolvers.add(new RegionResolver());
//        argumentResolvers.addAll(requestMappingHandlerAdapter.getArgumentResolvers());
//        requestMappingHandlerAdapter.setArgumentResolvers(argumentResolvers);
//    }

    @Bean
    public InfluxDBClient influxDBClient(InfluxProperties properties) {
        var builder = InfluxDBClientOptions.builder()
                .url(properties.getUrl())
                .org(properties.getOrg())
                .bucket(properties.getBucket())
                .logLevel(LogLevel.BASIC);
        if (Strings.isNullOrEmpty(properties.getUsername())) {
            builder.authenticateToken(properties.getToken());
        } else {
            builder.authenticate(properties.getUsername(), properties.getToken());
        }
        return  InfluxDBClientFactory.create(builder.build());
    }
}
