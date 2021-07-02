package ltd.fdsa.cloud.service;

import io.minio.messages.Bucket;

import java.io.InputStream;
import java.util.List;

public interface MinIOService {

    /**
     * 获取bucket列表
     *
     * @return
     */
    List<Bucket> bucketList();

    /**
     * 创建bucket
     *
     * @param bucketName
     */
    void makeBucket(String bucketName);

    /**
     * 删除bucket
     *
     * @param bucketName
     */
    void removeBucket(String bucketName);

    /**
     * 删除文件
     *
     * @param bucketName
     * @param objectName
     */
    void removeFile(String bucketName, String objectName);

    /**
     * 上传文件
     *
     * @param bucketName
     * @param objectName
     * @param filePath
     */
    void uploadFile(String bucketName, String objectName, String filePath);

    /**
     * 上传文件
     *
     * @param bucketName
     * @param objectName
     * @param stream
     * @param size
     * @param contentType
     */
    void uploadFile(String bucketName, String objectName, InputStream stream, long size, String contentType);

    boolean isFileExist(String bucketName, String objectName);

    /**
     * 获取下载流
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    InputStream downloadFile(String bucketName, String objectName);

    /**
     * 获取下载地址
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    String getDownloadUrl(String bucketName, String objectName);


}
