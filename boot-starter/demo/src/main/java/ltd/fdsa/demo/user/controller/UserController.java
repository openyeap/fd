package ltd.fdsa.demo.user.controller;

import ltd.fdsa.boot.starter.annotation.AuthRole;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2019/12/17 14:06
 * @Author 高进
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @AuthRole("admin,user")
    @PostMapping("/add")
    public String addUser() {
        return "我是新增用户";
    }

    @AuthRole("admin")
    @RequestMapping(value = "/upd", method = RequestMethod.POST)
    public String updUser() {
        return "我是修改用户";
    }

    @AuthRole("admin,user")
    @GetMapping("/list")
    public String getUserList() {
        return "我是用户列表";
    }

    @AuthRole("admin,user")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getUserInfo() {
        return "我是用户详情";
    }
}
