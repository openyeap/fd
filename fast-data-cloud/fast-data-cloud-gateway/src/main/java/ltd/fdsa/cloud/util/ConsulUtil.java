package ltd.fdsa.cloud.util;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Check;
import com.ecwid.consul.v1.agent.model.Service;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Classname ConsulUtil
 * @Description TODO
 * @Date 2019/12/13 9:52
 * @Author 高进
 */
@Slf4j
public class ConsulUtil {
    private ConsulUtil() {
    }

    private volatile static ConsulUtil consulUtil = null;

    public static ConsulUtil getInstance() {
        if (consulUtil == null) {
            synchronized (ConsulUtil.class) {
                if (consulUtil == null) {
                    consulUtil = new ConsulUtil();
                }
            }
        }
        return consulUtil;
    }

    public Map<String, ArrayList<ServiceInfo>> getHealthServices(ConsulClient consulClient) {
        if(consulClient == null) {
            log.info("注册服务信息传入错误！");
            return null;
        }
        Map<String, ArrayList<ServiceInfo>> map = new HashMap<>();
        Response<Map<String, Check>> serviceCheckResponse = consulClient.getAgentChecks();
        Response<Map<String,Service>> serviceResponse = consulClient.getAgentServices();
        for(Map.Entry<String,Check> entry : serviceCheckResponse.getValue().entrySet()) {
            if(Check.CheckStatus.PASSING == entry.getValue().getStatus()) {
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceId(entry.getValue().getServiceId());
                String serviceName = entry.getValue().getServiceName();
                serviceInfo.setServiceName(serviceName);
                Service service = serviceResponse.getValue().get(entry.getValue().getServiceId());
                serviceInfo.setServiceAddress(service.getAddress());
                serviceInfo.setServicePort(service.getPort());
                serviceInfo.setServiceUrl("http://" + serviceInfo.getServiceAddress() + ":" + serviceInfo.getServicePort());
                ArrayList<ServiceInfo> list = map.get(serviceName);
                if(list == null) {
                    list = new ArrayList<>();
                    list.add(serviceInfo);
                } else {
                    list = map.get(serviceName);
                    list.add(serviceInfo);
                }
                map.put(serviceName, list);
            }
        }
        return map;
    }

    @Getter
    @Setter
    public static class ServiceInfo {
        private String serviceId;
        private String serviceName;
        private String serviceAddress;
        private int servicePort;
        private String serviceUrl;
    }
}
