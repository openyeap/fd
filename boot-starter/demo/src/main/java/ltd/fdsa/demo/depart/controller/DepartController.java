package ltd.fdsa.demo.depart.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @Classname DepartController
 * @Description TODO
 * @Date 2019/12/17 14:01
 * @Author 高进
 */
@RestController
@RequestMapping({"/depart", "/dep"})
public class DepartController {

    @GetMapping("/list")
    public String getDepartList() {
        return "我是部门列表";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getDepartInfo() {
        return "我是部门详情";
    }

    @PostMapping("/delete")
    public String delDepartInfo() {
        return "我是删除部门";
    }

    @RequestMapping(value = "upd", method = RequestMethod.POST)
    public String updDepartInfo() {
        return "我是修改部门";
    }
}
