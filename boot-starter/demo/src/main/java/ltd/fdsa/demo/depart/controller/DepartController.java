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

    @GetMapping
    public String getDepartList() {
        return "我是部门列表";
    }

    @PutMapping
    public String createDepart() {
        return "我是新增部门";
    }

    @RequestMapping(value = "/{departId}", method = RequestMethod.GET)
    public String getDepartInfo(@PathVariable(value = "departId") int departId) {
        return "我是部门详情" + departId;
    }

    @DeleteMapping
    public String delDepartInfo() {
        return "我是删除部门";
    }

    @PostMapping
    public String updDepartInfo() {
        return "我是修改部门";
    }
}
