package ltd.fdsa.search.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController("x")
@Slf4j
public class TestController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    RequestMappingInfo requestMappingInfo;

    @RequestMapping("/registerBean")
    public Object search() throws NoSuchMethodException {
        if (this.requestMappingInfo != null) {
            return "No Action";
        }
        this.requestMappingInfo = RequestMappingInfo
                .paths("/one/demo")
                .methods(RequestMethod.GET)
                .build();


        requestMappingHandlerMapping.registerMapping(requestMappingInfo, this,
                this.getClass().getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class));

        return "OK";
    }

    public void test(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text");
        response.setHeader("test", "test");
        try {
            response.getWriter().println("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/removeBeanDefinition")
    public Object removeBeanDefinition() {
        if (this.requestMappingInfo != null) {
            requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
            this.requestMappingInfo = null;
        }
        return "OK";
    }


}
