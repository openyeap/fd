package ltd.fdsa.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname HelloController
 * @Description TODO
 * @Date 2019/12/16 11:12
 * @Author 高进
 */
@RestController
@Api(tags = "demo-hello接口")
@Slf4j
public class HelloController {
    @ApiOperation("这是一个接口")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
    	log.debug("Swagger");
        return "Hello Swagger UI";
    }
}
