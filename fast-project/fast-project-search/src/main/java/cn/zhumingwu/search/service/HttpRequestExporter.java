package cn.zhumingwu.search.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;


import java.io.IOException;

@Slf4j
public class HttpRequestExporter implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text");
        response.setHeader("test", "test");
        response.getWriter().println("test");
    }
}
