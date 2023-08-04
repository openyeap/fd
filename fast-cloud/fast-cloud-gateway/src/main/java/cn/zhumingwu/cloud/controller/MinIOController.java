package cn.zhumingwu.cloud.controller;

import com.google.common.base.Strings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import cn.zhumingwu.cloud.constant.Constant;
import cn.zhumingwu.cloud.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MinIOController {

    @Autowired
    private MinIOService minIOService;

    @PostMapping("/bucket/{bucketName}")
    public Mono<ResponseEntity<Object>> makeBucket(@PathVariable String bucketName) {
        if (Strings.isNullOrEmpty(bucketName)) {
            return Mono.just((new ResponseEntity<Object>(HttpStatus.BAD_REQUEST)));
        }
        minIOService.makeBucket(bucketName);
        return Mono.just((new ResponseEntity<Object>(HttpStatus.OK)));
    }

    @DeleteMapping("/bucket/{bucketName}")
    public Mono<ResponseEntity<Object>> removeBucket(@PathVariable String bucketName) {
        if (Strings.isNullOrEmpty(bucketName)) {
            return Mono.just((new ResponseEntity<Object>(HttpStatus.BAD_REQUEST)));
        }
        minIOService.removeBucket(bucketName);
        return Mono.just((new ResponseEntity<Object>(HttpStatus.OK)));
    }

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Object>> upload(@RequestPart("file") FilePart filePart, @RequestPart String bucketName, @RequestPart String path) {
        Map<String, String> map = new HashMap<>();
        if (Strings.isNullOrEmpty(bucketName)) {
            bucketName = Constant.Default_Bucket_Name;
        }
        String bkName = bucketName;
        if (Strings.isNullOrEmpty(path)) {
            path = "";
        } else {
            if (!path.endsWith("/")) {
                path += "/";
            }
        }
        String objectName = path + filePart.filename();
        String fileId = null;
        try {
            fileId = Constant.MinIO_Prefix
                    + Constant.MinIO_Split + bucketName
                    + Constant.MinIO_Split + Base64.getUrlEncoder().encodeToString(objectName.getBytes(Constant.UTF8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        map.put("fileId", fileId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataBufferUtils.write(filePart.content(), outputStream)
                .doOnComplete(() -> {
                    String contentType = filePart.headers().getContentType().toString();
                    InputStream stream = new ByteArrayInputStream(outputStream.toByteArray());
                    try {
                        long size = stream.available();
                        minIOService.uploadFile(bkName, objectName, stream, size, contentType);
                    } catch (Exception e) {
                        log.error("文件上传异常", e.getMessage());
                        return;
                    }
                }).subscribe();
        return Mono.just(ResponseEntity.ok().body(map));
    }

    @GetMapping(value = "/file/download")
    public Mono<ResponseEntity<Object>> download(String fileId) {

        try {
            if (Strings.isNullOrEmpty(fileId)) {
                return Mono.just((new ResponseEntity<Object>(HttpStatus.BAD_REQUEST)));
            }
            String[] split = fileId.split("\\" + Constant.MinIO_Split);
            if (split == null || split.length != 3 || !Constant.MinIO_Prefix.equals(split[0])) {
                return Mono.just((new ResponseEntity<Object>(HttpStatus.BAD_REQUEST)));
            }
            String bucketName = split[1];
            String objectName = new String(Base64.getUrlDecoder().decode(split[2]), Constant.UTF8);
            String fileName = java.net.URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), Constant.UTF8);
            InputStream inputStream = minIOService.downloadFile(bucketName, objectName);
            if (inputStream == null) {
                return Mono.just((new ResponseEntity<Object>(HttpStatus.NO_CONTENT)));
            }
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers.add("Content-Encode", Constant.UTF8);
            return Mono.just(ResponseEntity.ok()
                    .headers(headers)
                    .body(inputStreamResource));
        } catch (Exception e) {
            log.error("文件读取异常", e);
            return Mono.just((new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }
}
