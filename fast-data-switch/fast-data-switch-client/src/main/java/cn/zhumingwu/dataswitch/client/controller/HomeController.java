package cn.zhumingwu.dataswitch.client.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    public String index(Model model) {
        return "hi";
    }

}
