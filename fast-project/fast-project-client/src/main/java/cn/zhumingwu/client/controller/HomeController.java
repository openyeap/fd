package cn.zhumingwu.client.controller;

import cn.zhumingwu.client.service.SayService;
import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.api.service.HiService;
import cn.zhumingwu.base.context.ApplicationContextHolder;
import cn.zhumingwu.base.event.RefreshedEvent;
import cn.zhumingwu.base.event.RemotingEvent;
import cn.zhumingwu.base.serializer.HessianSerializer;
import cn.zhumingwu.base.serializer.JavaSerializer;
import cn.zhumingwu.base.serializer.JsonSerializer;
import cn.zhumingwu.base.serializer.KryoSerializer;
import cn.zhumingwu.starter.remote.annotation.RpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class HomeController {

    @RpcClient
    @Autowired
    private HiService hiService;


    @Autowired
    List<SayService> sayServiceList;

    @GetMapping("test")
    public Object test() {
        this.sayServiceList.forEach(person -> {
            person.say("test");
        });
        return "test";
    }

    @GetMapping(value = "/rpc/api")
    public Object rpcApi() {
        log.debug(this.hiService.getClass().getCanonicalName());

        return new Object[]{
                this.hiService.getClass().getName(),
                this.hiService.hi("rpc api")
        };
    }


    @GetMapping(value = "/publishEvent")
    public Object publishEvent(@RequestParam(required = false) Optional<String> name) {
        switch (name.get()) {
            case "h":
                RemotingEvent.SERIALIZER = new HessianSerializer();
                break;
            case "j":
                RemotingEvent.SERIALIZER = new JsonSerializer();
                break;
            case "k":
                RemotingEvent.SERIALIZER = new KryoSerializer();
                break;
            default:
                RemotingEvent.SERIALIZER = new JavaSerializer();
                break;
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            map.put("key" + i, "value" + i);
        }
        var event = new RefreshedEvent(this, map);

        ApplicationContextHolder.publishRemote(event);
        return map;
    }

}
