package cn.zhumingwu.client.controller;

import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.client.entity.DatasetAnnotation;
import cn.zhumingwu.client.service.DatasetAnnotationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("datasetannotation")
@Slf4j
public class DatasetAnnotationController extends BaseController {
    @Autowired
    private DatasetAnnotationService service;

    @GetMapping(value = "/list")
    public Result<Object> list() {
        return Result.success(this.service.findAll(""));
    }

    @PostMapping(value = "/insert")
    public Result<Object> insert(@RequestBody DatasetAnnotation entity) {
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
    public Result<Object> update(@RequestBody DatasetAnnotation entity) {
        try {
            return Result.success(this.service.update(entity));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }

}
