package ltd.fdsa.boot.starter.prometheus;

 
 
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

@Endpoint(id = "zhumingwu")
public class PrometheusActuatorEndpoint {

    @ReadOperation()
    public Map<String,String> read(){
        Map<String,String> result = new HashMap<>();
        result.put("name","zhumingwu");
        result.put("age","38");
        return result;
    }

} 