package ltd.fdsa.cloud.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.constant.Constant;
import ltd.fdsa.cloud.service.MinIOService;
import ltd.fdsa.web.enums.HttpCode;
import ltd.fdsa.web.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MinIOController {

    @Autowired
    private MinIOService minIOService;

    @PostMapping("/bucket/{bucketName}")
    public Result<Object> makeBucket(@PathVariable String bucketName) {
        if (StringUtils.isEmpty(bucketName)) {
            return Result.fail(HttpCode.PARAMETER_EMPTY);
        }
        minIOService.makeBucket(bucketName);
        return Result.success();
    }

    @DeleteMapping("/bucket/{bucketName}")
    public Result<Object> removeBucket(@PathVariable String bucketName) {
        if (StringUtils.isEmpty(bucketName)) {
            return Result.fail(HttpCode.PARAMETER_EMPTY);
        }
        minIOService.removeBucket(bucketName);
        return Result.success();
    }

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SneakyThrows
    public Mono<ResponseEntity> upload(@RequestPart("file") FilePart filePart, @RequestPart String bucketName, @RequestPart String path) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(bucketName)) {
            bucketName = Constant.Default_Bucket_Name;
        }
        String bkName = bucketName;
        if (StringUtils.isEmpty(path)) {
            path = "";
        } else {
            if (!path.endsWith("/")) {
                path += "/";
            }
        }
        String objectName = path + filePart.filename();
        String fileId = Constant.MinIO_Prefix
                + Constant.MinIO_Split + bucketName
                + Constant.MinIO_Split + Base64.getUrlEncoder().encodeToString(objectName.getBytes(Constant.UTF8));
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
        return Mono.just(ResponseEntity.ok().body(Result.success(map)));
    }

    @GetMapping(value = "/file/download")
    public Mono<ResponseEntity> download(String fileId) {
        return Mono.fromCallable(() -> {
            try {
                if (StringUtils.isEmpty(fileId)) {
                    return ResponseEntity.ok().body(Result.fail(HttpCode.PARAMETER_EMPTY));
                }
                String[] split = fileId.split("\\" + Constant.MinIO_Split);
                if (split == null || split.length != 3 || !Constant.MinIO_Prefix.equals(split[0])) {
                    return ResponseEntity.ok().body(Result.fail(HttpCode.PARAMETER_INCORRECT));
                }
                String bucketName = split[1];
                String objectName = new String(Base64.getUrlDecoder().decode(split[2]), Constant.UTF8);
                String fileName = java.net.URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), Constant.UTF8);
                InputStream inputStream = minIOService.downloadFile(bucketName, objectName);
                if (inputStream == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.fail(HttpCode.NOT_FOUND));
                }
                InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", "attachment;filename=" + fileName);
                headers.add("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
                headers.add("Content-Encode", Constant.UTF8);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(inputStreamResource);
            } catch (Exception e) {
                log.error("文件读取异常", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
            }
        });
    }
}
