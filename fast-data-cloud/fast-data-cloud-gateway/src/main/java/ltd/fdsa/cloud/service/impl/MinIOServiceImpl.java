package ltd.fdsa.cloud.service.impl;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.service.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MinIOServiceImpl implements MinIOService {
    @Autowired
    private MinioClient minioClient;

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!minioClient.bucketExists(bucketName)) {
            minioClient.makeBucket(bucketName);
        }
    }

    /**
     * 获取全部bucket
     *
     * <p>https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    @Override
    public List<Bucket> bucketList() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public void makeBucket(String bucketName) {
        try {
            if (!minioClient.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 根据bucketName删除信息
     *
     * @param bucketName bucket名称
     */
    @Override
    public void removeBucket(String bucketName) {
        try {
            if (minioClient.bucketExists(bucketName)) {
                Iterable<Result<Item>> iterable = minioClient.listObjects(bucketName);
                for (Iterator iterator = iterable.iterator(); iterator.hasNext(); ) {
                    Result<Item> result = (Result<Item>) iterator.next();
                    minioClient.removeObject(bucketName, result.get().objectName());
                }
                minioClient.removeBucket(bucketName);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void removeFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(bucketName, objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void uploadFile(String bucketName, String objectName, String filePath) {
        try {
            if (!minioClient.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
            }
//            minioClient.putObject(bucketName, objectName, filePath);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void uploadFile(String bucketName, String objectName, InputStream stream, long size, String contentType) {
        try {
            if (!minioClient.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
            }
//            minioClient.putObject(bucketName, objectName, stream, size, contentType);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean isFileExist(String bucketName, String objectName) {
        try {
            return minioClient.statObject(bucketName, objectName) != null;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(bucketName, objectName);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String getDownloadUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(Method.GET, bucketName, objectName, 24 * 60 * 60, null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    @SneakyThrows
    public List<Result<Item>> getAllObjectsByPrefix(
            String bucketName, String prefix, boolean recursive) {
        List<Result<Item>> objectList = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(bucketName, prefix, recursive);

        while (objectsIterator.iterator().hasNext()) {
            objectList.add(objectsIterator.iterator().next());
        }
        return objectList;
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    单位秒
     * @return url
     */
    @SneakyThrows
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        return minioClient.presignedGetObject(bucketName, objectName, expires);
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        return minioClient.getObject(bucketName, objectName);
    }

//    @SneakyThrows
//    public boolean putObjectWithRetry(
//            String bucketName,
//            String objectName,
//            InputStream stream,
//            Long size,
//            Map<String, String> headerMap,
//            ServerSideEncryption sse,
//            String contentType) {
//        int current = 0;
//        while (current < 3) {
//            PutObjectOptions options = new PutObjectOptions(size, 0);
//            options.setHeaders(headerMap);
//            options.setSse(sse);
//            options.setContentType(contentType);
//            minioClient.putObject(bucketName, objectName, stream, options);
//            current++;
//            return true;
//        }
//        return false;
//    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contentType 类型
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
//    public boolean putObject(            String bucketName, String objectName, InputStream stream, long size, String contentType) {
//        Map<String, String> headerMap = null;
//        ServerSideEncryption sse = null;
//        return this.putObjectWithRetry(                bucketName, objectName, stream, size, headerMap, sse, contentType);
//    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    @SneakyThrows(Exception.class)
    public ObjectStat getObjectInfo(String bucketName, String objectName) {
        return minioClient.statObject(bucketName, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    @SneakyThrows(Exception.class)
    public void removeObject(String bucketName, String objectName) {
        minioClient.removeObject(bucketName, objectName);
    }
}
