package cn.zhumingwu.cloud.controller;
 
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.cloud.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class HomeController {
    @Autowired
    RouteDefinitionLocator routeService;
    @Autowired
    MinIOService minioService;
    @Autowired
    private DiscoveryClient discoveryClient;
 

    @GetMapping(value = "/")
    public Mono<ResponseEntity<String>> echo() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Encode", "UTF-8");
        headers.add("Location", "/swagger-ui.html");
        ResponseEntity<String> response = new ResponseEntity<String>("", headers, HttpStatus.TEMPORARY_REDIRECT);
        return Mono.just(response);
    }

    @RequestMapping(value = "/routes", method = RequestMethod.GET)
    public Object listRoute() {
        List<Object> list = new ArrayList<Object>();
        routeService.getRouteDefinitions().subscribe(r -> {
            list.add(r);
        });
        return list;
    }

    @PostMapping("/watch")
    public Object watchChanges(@RequestBody Object body, @RequestHeader HttpHeaders header) {

        StringBuffer str = new StringBuffer();
        if (body != null) {
            str.append(body.toString());
        }

        if (header != null) {
            str.append(header.toString());
        }
        return str;
    }

    @RequestMapping("/services")
    public Object serviceUrl() {
        List<Object> list = new ArrayList<Object>();
        for (String item : this.discoveryClient.getServices()) {
            list.addAll(this.discoveryClient.getInstances(item));
        }

        return list;
    }


    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @SneakyThrows
    public void upload(
            @RequestPart("file") FilePart filePart,
            @RequestPart("bucketName") String bucketName,
            @RequestPart("path") String path) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataBufferUtils.write(filePart.content(), outputStream)
                .doOnComplete(
                        () -> {
                            try {
                            } catch (Exception ex) {
                                log.error(ex.getMessage());
                            }
                        })
                .subscribe();
    }

    @RequestMapping(value = { "/file/{bucket}/{path}", "/file/{bucket}/*/{path}" }, method = RequestMethod.GET)
    public Mono<ResponseEntity<Object>> file(
            @PathVariable(value = "bucket") String bucketName,
            @PathVariable(value = "path") String fileName,
            ServerHttpRequest request) {

        final String objectName = request.getURI().getPath().substring(bucketName.length() + 7);
        return Mono.fromCallable(
                () -> {
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.add(
                                "Content-Disposition",
                                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                        headers.add("Content-Encode", "UTF-8");
                        InputStream inputStream = this.minioService.downloadFile(bucketName, objectName);
                        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                        return ResponseEntity.ok()
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .body(inputStreamResource);
                    } catch (Exception e) {
                        log.error("文件读取异常", e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                    }
                });
    }
}
