package ltd.fdsa.server.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.api.service.HiService;
import ltd.fdsa.core.context.ApplicationContextHolder;
import ltd.fdsa.core.event.RefreshedEvent;
import ltd.fdsa.core.util.ProxyUtils;
import ltd.fdsa.server.service.impl.HiServiceImpl;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class HomeController {

    @Autowired
    HiServiceImpl service;

    @GetMapping({"/"})
    public Result<Object> home() {
        return Result.success(service.hi("HashMap"));
    }


    @GetMapping({"/getProxyByJDK"})
    public Object getProxyByJDK() {
        var serviceImpl = new HiServiceImpl();
        var service = ProxyUtils.getProxyByJDK(serviceImpl, HiService.class);
        service = ProxyUtils.getProxyByJDK(serviceImpl);
        return service.hi("test");

    }

    private RestTemplate restTemplate;

    @GetMapping({"/getProxyByCglib"})
    public Object getProxyByCglib() {
        var serviceImpl = new HiServiceImpl();

        var service = ProxyUtils.getProxyByCglib(serviceImpl);
        return service.hi("test");
    }


    @GetMapping(value = "/publishEvent")
    public Object publishEvent(@RequestParam(required = false) String name) {
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            data.put("key" + i, "value" + i);
        }
        var contextRefreshedEvent = new RefreshedEvent(this, data);
        ApplicationContextHolder.publishRemote(contextRefreshedEvent);
        return data;
    }
}