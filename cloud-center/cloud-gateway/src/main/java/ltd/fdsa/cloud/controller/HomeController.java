package ltd.fdsa.cloud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ltd.fdsa.cloud.util.Base64Util;
import ltd.fdsa.cloud.util.ConsulServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.service.DynamicRouteService;

@RestController
@Slf4j
public class HomeController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    DynamicRouteService routeService;

    @Autowired
    ConsulClient consulClient;

    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }

    @RequestMapping("/reload")
    public String reload() {
        routeService.notifyChanges();
        return "Ok";
    }

    @PostMapping("/watch")
    public Object watchChanges(@RequestBody Object body, @RequestHeader HttpHeaders header)
            throws JsonProcessingException {
        routeService.notifyChanges();
        StringBuffer str = new StringBuffer();
        if (body != null) {
            str.append(body.toString());
//			log.info(body.toString());
        }

        if (header != null) {
            str.append(header.toString());
//			log.info(header.toString());
        }
//		log.info(header.toString());
//		ObjectMapper mapper = new ObjectMapper();
//		  str = mapper.writeValueAsString(s);

        return str;
    }

    @RequestMapping("/services")
    public Object serviceUrl() {
        List<Object> list = new ArrayList<Object>();
        for (String item : this.discoveryClient.getServices()) {
            list.addAll(this.discoveryClient.getInstances(item));
        }

        Map<String, Service> services = consulClient.getAgentServices().getValue();
        for (String key : services.keySet()) {
            list.add(key);
            list.add(services.get(key));
        }
        return list;
    }


    @RequestMapping("/register")
    public Object registerService() {

        for (int i = 0; i < 10; i++) {
            NewService service = new NewService();
            service.setAddress("http://192.168.100.1" + Integer.toString(i));
            service.setId("id" + Integer.toString(i));
            service.setName("service-name");
            service.setPort(8080 + i);

            // service.setTags(tags);
            consulClient.agentServiceRegister(service);
        }

        Map<String, Service> services = consulClient.getAgentServices().getValue();
        return services;
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public ConcurrentHashMap<String, String> initConsulKV() {
        ConsulServerUtil.getInstance().init(consulClient);
        return ConsulServerUtil.getInstance().getALLConsulKV();
    }

    @RequestMapping(value = "/authWatch", method = RequestMethod.POST)
    public ConcurrentHashMap<String, String> consulKVChanges(@RequestBody List<HashMap<String, String>> list) {
        Map<String, String> map = new HashMap<>();
        for (HashMap<String, String> info : list) {
            map.put(info.get("Key"), info.get("Value") == null ? "" : Base64Util.decode(info.get("Value")));
        }
        ConsulServerUtil.getInstance().updConsulKV(map);
        return ConsulServerUtil.getInstance().getALLConsulKV();
    }

    @RequestMapping(value = "/getKV", method = RequestMethod.GET)
    public ConcurrentHashMap<String, String> getConsulKVs() {
        return ConsulServerUtil.getInstance().getALLConsulKV();
    }

    @RequestMapping(value = "/serviceWatch", method = RequestMethod.POST)
    public ConcurrentHashMap<String, ConsulServerUtil.ServerInfo> consulServiceChanges(@RequestBody Object obj) {
        ConsulServerUtil.getInstance().updConsulService(consulClient);
        return ConsulServerUtil.getInstance().getAllConsulService();
    }

    @RequestMapping(value = "/getService", method = RequestMethod.GET)
    public ConcurrentHashMap<String, ConsulServerUtil.ServerInfo> getConsulServices() {
        return ConsulServerUtil.getInstance().getAllConsulService();
    }
}
