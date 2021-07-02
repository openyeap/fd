package ltd.fdsa.search.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import ltd.fdsa.core.context.ApplicationContextHolder;
import ltd.fdsa.core.util.NamingUtils;
import ltd.fdsa.search.model.MarkdownDoc;
import ltd.fdsa.search.model.entity.ElasticSearchDoc;
import ltd.fdsa.search.model.view.ViewSource;
import ltd.fdsa.search.service.HttpRequestExporter;
import ltd.fdsa.search.service.ItemService;
import ltd.fdsa.search.service.RestHighLevelClientService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

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
