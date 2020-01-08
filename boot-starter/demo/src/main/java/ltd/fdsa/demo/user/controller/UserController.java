package ltd.fdsa.demo.user.controller;

import ltd.fdsa.boot.starter.annotation.AuthRole;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Classname UserController
 * @Description TODO
 * @Date 2019/12/17 14:06
 * @Author 高进
 */
//@RestController
@RequestMapping("/user")
public class UserController {

    @AuthRole("admin,user")
    @PutMapping
    public String addUser(@RequestBody Map<String, Object> data) {
        return "我是新增用户" + data;
    }

//    @DeleteMapping("/{table}")
    public String delUser(@PathVariable String table, @RequestParam(defaultValue = "") String where) {
        return "删除用户" + table;
    }

    @AuthRole("admin")
//    @PostMapping("/{table}")
    public String updUser(@PathVariable String table, @RequestParam(defaultValue = "") String where, @RequestBody Map<String, Object> data) {
        return "修改用户" + table;
    }

    @AuthRole("admin,user")
    @GetMapping
    public String getUserList(@PathVariable String table, @RequestParam(defaultValue = "*") String select,
                              @RequestParam(defaultValue = "") String query, @RequestParam(defaultValue = "") String order,
                              @RequestParam(defaultValue = "") String group, @RequestParam(defaultValue = "") String having,
                              @RequestParam(required = false, defaultValue = "0") int page,
                              @RequestParam(required = false, defaultValue = "20") int size) {
        return "查询用户列表" + page;
    }

    @AuthRole("admin,user")
    @GetMapping("/{id}")
    public String getUserInfo(@RequestParam int id) {
        return "查询用户" + id;
    }
}
