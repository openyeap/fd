package cn.zhumingwu.client.controller;

import lombok.extern.slf4j.Slf4j;

import cn.zhumingwu.client.jpa.entity.Group;
import cn.zhumingwu.client.jpa.service.GroupService;
import cn.zhumingwu.database.entity.Status;
import cn.zhumingwu.web.controller.BaseController;
import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.base.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("group")
@Slf4j
public class GroupController extends BaseController {
    @Autowired
    private GroupService service;

    @GetMapping(value = "/list")
    public Result<Object> list() {
        return Result.success(this.service.findAll());
    }


    @GetMapping(value = "/insert")
    public Result<Object> insert(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Integer sort,
            @RequestParam(required = false) Byte type,
            @RequestParam(required = false) Status status
    ) {
        try {
            var entity = new Group();
            entity.setName(name);
            entity.setRemark(remark);
            entity.setSort(sort);
            entity.setStatus(status);
            entity.setType(type);
//            user.setCreateTime(LocalDateTime.now());
//            user.setUpdateTime(LocalDateTime.now());
            return Result.success(this.service.insert(entity));
        } catch (Exception ex) {
            return Result.fail(HttpCode.BAD_REQUEST, ex);
        }
    }

    @GetMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable(value = "id") Integer id) {
        this.service.deleteById(id);
        return Result.success();
    }

    @GetMapping(value = "/update")
    public Result<Object> update(
            @RequestParam(required = true) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Integer sort,
            @RequestParam(required = false) Byte type,
            @RequestParam(required = false) Status status
    ) {
        try {
            var entity = new Group();
            entity.setId(id);
            entity.setName(name);
            entity.setRemark(remark);
            entity.setSort(sort);
            entity.setStatus(status);
            entity.setType(type);
//            user.setCreateTime(LocalDateTime.now());
//            user.setUpdateTime(LocalDateTime.now());
            return Result.success(this.service.update(entity));
        } catch (Exception ex) {
            return Result.fail(HttpCode.BAD_REQUEST, ex);
        }
    }

}
