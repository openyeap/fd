package cn.zhumingwu.cloud.service.impl;

import cn.zhumingwu.cloud.service.MinIOService;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MinIOServiceImpl implements MinIOService {
    @Autowired
    private MinioClient minioClient;


    public void createBucket(String bucketName) {
        if (!minioClient.bucketExists(bucketName)) {
            minioClient.makeBucket(bucketName);
        }
    }


    public List<Bucket> getAllBuckets() {
        return minioClient.listBuckets();
    }


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

    @Override
    public void removeBucket(String bucketName) {
        try {
            if (minioClient.bucketExists(bucketName)) {
                Iterable<Result<Item>> iterable = minioClient.listObjects(bucketName);
                for (Result<Item> result : iterable) {
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


    public List<Result<Item>> getAllObjectsByPrefix(
            String bucketName, String prefix, boolean recursive) {
        List<Result<Item>> objectList = new ArrayList<>();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(bucketName, prefix, recursive);

        while (objectsIterator.iterator().hasNext()) {
            objectList.add(objectsIterator.iterator().next());
        }
        return objectList;
    }


    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        return minioClient.presignedGetObject(bucketName, objectName, expires);
    }


    public InputStream getObject(String bucketName, String objectName) {
        return minioClient.getObject(bucketName, objectName);
    }

(Exception.class)
    public ObjectStat getObjectInfo(String bucketName, String objectName) {
        return minioClient.statObject(bucketName, objectName);
    }

(Exception.class)
    public void removeObject(String bucketName, String objectName) {
        minioClient.removeObject(bucketName, objectName);
    }
}
