package cn.zhumingwu.dataswitch.admin.controller;


import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.dataswitch.core.store.EventMemoryTable;
import cn.zhumingwu.dataswitch.core.store.EventMessage;
import lombok.var;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.StandardCharsets;

/**
 * job code controller
 */
@Controller
@RequestMapping("/api")
public class ApiController extends BaseController {
    EventMemoryTable eventMemoryTable = new EventMemoryTable();

    @RequestMapping("/push")
    @ResponseBody
    public Result<Long> save(String topic, String body) {
        var eventMessage = new EventMessage(topic, body.getBytes(StandardCharsets.UTF_8));
        return Result.success(eventMemoryTable.put(eventMessage));
    }

    @RequestMapping("/pull")
    @ResponseBody
    public Result<EventMessage> save(String topic, Long offset) {
        var eventMessage = eventMemoryTable.get(topic, offset);
        return Result.success(eventMessage);
    }

}
