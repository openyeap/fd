package cn.zhumingwu.dataswitch.admin.controller;

import cn.zhumingwu.dataswitch.core.job.coordinator.Coordinator;
import cn.zhumingwu.dataswitch.admin.annotation.PermissionLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class RxController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private Coordinator coordinator;


    @RequestMapping("/coordinator")
    @ResponseBody
    public HessianServiceExporter exportHelloHessian()
    {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(coordinator);
        exporter.setServiceInterface(Coordinator.class);
        return exporter;
    }
    @RequestMapping("/uid")
    @ResponseBody
    @PermissionLimit(limit = false)
    public String index() {
        Object obj = redisTemplate.opsForValue().get("test");

        return this.stringRedisTemplate.opsForValue().get("test");
    }

    @RequestMapping("/publish")
    public String publish() {

        for (int i = 0; i < 10; i++) {
            stringRedisTemplate.convertAndSend("mytopic", "这是我发第" + i + "条的消息啊");
        }
        return "结束";
    }
}
