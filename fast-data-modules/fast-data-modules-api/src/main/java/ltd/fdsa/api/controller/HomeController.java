package ltd.fdsa.api.controller;

import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.api.service.HiService;
import ltd.fdsa.starter.remote.annotation.RpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class HomeController {

    @RpcClient("${project.remote.service.name:${spring.application.name:default}}")
    @Autowired
    HiService service;

    @GetMapping(value = "/")
    public Object home() {

        return this.service.hi("test");
    }


}
