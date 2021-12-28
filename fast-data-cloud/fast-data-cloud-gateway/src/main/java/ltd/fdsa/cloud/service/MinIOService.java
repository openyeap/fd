package ltd.fdsa.cloud.service;

import io.minio.messages.Bucket;

import java.io.InputStream;
import java.util.List;

public interface MinIOService {

    List<Bucket> bucketList();

    void makeBucket(String bucketName);

    void removeBucket(String bucketName);

    void removeFile(String bucketName, String objectName);

    void uploadFile(String bucketName, String objectName, String filePath);

    void uploadFile(String bucketName, String objectName, InputStream stream, long size, String contentType);

    boolean isFileExist(String bucketName, String objectName);

    InputStream downloadFile(String bucketName, String objectName);

    String getDownloadUrl(String bucketName, String objectName);

}
