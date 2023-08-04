package cn.zhumingwu.client.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public  class HomeController {

    @RequestMapping(value = {"/", "/index"})
    public String index(Object obj) {
        return "index";
    }
}
