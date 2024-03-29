package cn.zhumingwu.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestHandler;

import jakarta.servlet.ServletException;


import java.io.IOException;

@Slf4j
public class HttpRequestExporter implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text");
        response.setHeader("tet", "tset");
        response.getWriter().println("test");
    }
}
