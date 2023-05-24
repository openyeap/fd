package cn.zhumingwu.starter.monitor.prometheus;

import com.alibaba.druid.pool.DruidDataSourceStatLogger;
import com.alibaba.druid.pool.DruidDataSourceStatLoggerAdapter;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import cn.zhumingwu.starter.monitor.converter.DruidStatConverter;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Endpoint(id = "druid")
@Data
public class PrometheusDruidStatEndpoint extends DruidDataSourceStatLoggerAdapter implements DruidDataSourceStatLogger {

    Map<String, String> statValueMap = new HashMap<>();

    @ReadOperation(produces = {"text/plain"})
    public String read() {
        StringBuilder sb = new StringBuilder();
        this.statValueMap.forEach((k, v) -> {
            sb.append(v + "\n");
        });
        return sb.toString();
    }

    @Override
    public void log(DruidDataSourceStatValue statValue) {
        var result = new DruidStatConverter(statValue);
        this.statValueMap.put(statValue.getName(), result.toString());
    }
}