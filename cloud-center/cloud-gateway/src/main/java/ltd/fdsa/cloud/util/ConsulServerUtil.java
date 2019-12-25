package ltd.fdsa.cloud.util;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.Check;
import com.ecwid.consul.v1.agent.model.Service;
import com.ecwid.consul.v1.kv.model.GetValue;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname ConsulServerUtil
 * @Description TODO
 * @Date 2019/12/19 14:53
 * @Author 高进
 */
@Log4j2
public class ConsulServerUtil {

    private ConsulServerUtil() {

    }

    private volatile static ConsulServerUtil consul = null;

    public static ConsulServerUtil getInstance() {
        if (consul == null) {
            synchronized (ConsulServerUtil.class) {
                if (consul == null) {
                    consul = new ConsulServerUtil();
                }
            }
        }
        return consul;
    }

    /**
     * 监听KV数据
     */
    private ConcurrentHashMap<String, String> consulKV = null;

    /**
     * 监听Services数据
     */
    private ConcurrentHashMap<String, ServerInfo> consulServer = null;

    /**
     * Gateway启动时候，初始化KV数据
     */
    public void init(ConsulClient consulClient) {
        //初始化KV
        initConsulKV(consulClient);
        //初始化服务
        initConsulService(consulClient);
    }

    private void initConsulKV(ConsulClient consulClient) {
        consulKV = new ConcurrentHashMap<>();
        Response<List<GetValue>> response = consulClient.getKVValues("auth/");
        if (response != null && response.getValue() != null) {
            for (GetValue gv : response.getValue()) {
                consulKV.put(gv.getKey(), gv.getValue() == null ? "" : Base64Util.decode(gv.getValue()));
            }
        }
    }

    private void initConsulService(ConsulClient consulClient) {
        consulServer = new ConcurrentHashMap<>();
        Response<Map<String, Service>> agentServices = consulClient.getAgentServices();
        if (agentServices != null && agentServices.getValue() != null) {
            for (Map.Entry<String, Service> entry : agentServices.getValue().entrySet()) {
                if (entry == null) {
                    continue;
                }
                //设置基本信息
                ServerInfo serverInfo = new ServerInfo();
                serverInfo.setId(entry.getValue().getId());
                serverInfo.setService(entry.getValue().getService());
                serverInfo.setTags(entry.getValue().getTags());
                serverInfo.setAddress(entry.getValue().getAddress());
                serverInfo.setPort(entry.getValue().getPort());
                serverInfo.setUrl("http://" + serverInfo.getAddress() + ":" + serverInfo.getPort());
                consulServer.put(serverInfo.getId(), serverInfo);
            }
        }
        Response<Map<String, Check>> agentChecks = consulClient.getAgentChecks();
        if (agentChecks != null && agentChecks.getValue() != null) {
            for (Map.Entry<String, Check> entry : agentChecks.getValue().entrySet()) {
                if (entry == null) {
                    continue;
                }
                if (entry.getValue().getStatus() == Check.CheckStatus.PASSING) {
                    consulServer.get(entry.getValue().getServiceId()).setStatus("1");
                } else {
                    consulServer.get(entry.getValue().getServiceId()).setStatus("0");
                }
            }
        }
    }

    /**
     * watch kv 变化时候调用
     *
     * @param map
     */
    public void updConsulKV(Map<String, String> map) {
        consulKV.putAll(map);
    }

    /**
     * 获取kv对象
     *
     * @return
     */
    public ConcurrentHashMap<String, String> getALLConsulKV() {
        return consulKV;
    }

    /**
     * watch service 变化时候调用
     * <p>
     * 暂时先初始化服务，watch传输过来的信息暂时没啥用
     */
    public void updConsulService(ConsulClient consulClient) {
        initConsulService(consulClient);
    }

    /**
     * 获取服务信息
     *
     * @return
     */
    public ConcurrentHashMap<String, ServerInfo> getAllConsulService() {
        return consulServer;
    }

    @Data
    public static class ServerInfo {
        private String id;
        private String service;
        private List<String> tags;
        private String address;
        private int port;
        private String url;
        private String status;//0-不可访问，1-可以访问
    }
}
