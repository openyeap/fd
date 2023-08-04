package cn.zhumingwu.client.controller;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.client.entity.ClientMenu;
import cn.zhumingwu.client.service.ClientMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("clientmenu")
@Slf4j
public class ClientMenuController extends BaseController {
    @Autowired
    private ClientMenuService service;

    @GetMapping(value = "/list")
    public Result<Object> list() {
        return Result.success(this.service.findAll(""));
    }

    @PostMapping(value = "/insert")
    public Result<Object> insert(@RequestBody ClientMenu entity) {
        try {
            return Result.success(this.service.insert(entity));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

    @GetMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable(value = "id") Integer id) {
        this.service.deleteById(id);
        return Result.success();
    }

    @GetMapping(value = "/update")
    public Result<Object> update(@RequestBody ClientMenu entity) {
        try {
            return Result.success(this.service.update(entity));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

}
