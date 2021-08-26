package ltd.fdsa.kafka.stream.controller;

import ltd.fdsa.kafka.stream.entity.EtlTask;
import ltd.fdsa.kafka.stream.view.EtlTaskReq;
import ltd.fdsa.kafka.stream.service.IEtlTaskService;
import io.swagger.annotations.ApiOperation;
import ltd.fdsa.web.controller.BaseController;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Pivotal
 * @since 2021-04-06
 */
@RestController
@RequestMapping("etl-task")
public class EtlTaskController extends BaseController {
    @Autowired
    protected IEtlTaskService service;

    @ApiOperation("新增任务")
    @PostMapping
    public Result add(@RequestBody EtlTask etlTask) {

        service.insert(etlTask);
        return Result.success(etlTask);
    }

    @ApiOperation("任务查看")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id) {
        return Result.success(service.findById(id));
    }

    @ApiOperation("任务编辑")
    @PutMapping
    public Result edit(@RequestBody EtlTask etlTask) {
        return Result.success(service.update(etlTask));
    }

    @ApiOperation("任务删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {

        try {
            service.deleteById(id);
            return Result.success();
        } catch (Exception exception) {
            return Result.error(exception);
        }
    }

    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody EtlTaskReq etlTaskReq) {
        return Result.success(service.pageList(etlTaskReq));
    }

}

