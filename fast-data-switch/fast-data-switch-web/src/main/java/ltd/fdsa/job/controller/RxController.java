//package ltd.fdsa.job.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class RxController {
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @RequestMapping("/uid")
//    @ResponseBody
//    public String index() {
//        Object obj = redisTemplate.opsForValue().get("test");
//        return this.stringRedisTemplate.opsForValue().get("test");
//    }
//
//    @RequestMapping("/publish")
//    public String publish() {
//        for (int i = 0; i < 10; i++) {
//            stringRedisTemplate.convertAndSend("mytopic", "这是我发第" + i + "条的消息啊");
//        }
//        return "结束";
//    }
//}
