package ltd.fdsa.cloud.service;

import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class MinioService {
  @Autowired MinioClient client;

  /**
   * 创建bucket
   *
   * @param bucketName bucket名称
   */
  @SneakyThrows
  public void createBucket(String bucketName) {
    if (!client.bucketExists(bucketName)) {
      client.makeBucket(bucketName);
    }
  }

  /**
   * 获取全部bucket
   *
   * <p>https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
   */
  @SneakyThrows
  public List<Bucket> getAllBuckets() {
    return client.listBuckets();
  }

  /**
   * 根据bucketName获取信息
   *
   * @param bucketName bucket名称
   */
  @SneakyThrows
  public Optional<Bucket> getBucket(String bucketName) {
    return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
  }

  /**
   * 根据bucketName删除信息
   *
   * @param bucketName bucket名称
   */
  @SneakyThrows
  public void removeBucket(String bucketName) {
    client.removeBucket(bucketName);
  }

  /**
   * 根据文件前置查询文件
   *
   * @param bucketName bucket名称
   * @param prefix 前缀
   * @param recursive 是否递归查询
   * @return MinioItem 列表
   */
  @SneakyThrows
  public List<Result<Item>> getAllObjectsByPrefix(
      String bucketName, String prefix, boolean recursive) {
    List<Result<Item>> objectList = new ArrayList<>();
    Iterable<Result<Item>> objectsIterator = client.listObjects(bucketName, prefix, recursive);

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
   * @param expires 单位秒
   * @return url
   */
  @SneakyThrows
  public String getObjectURL(String bucketName, String objectName, Integer expires) {
    return client.presignedGetObject(bucketName, objectName, expires);
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
    return client.getObject(bucketName, objectName);
  }

  @SneakyThrows
  public boolean putObjectWithRetry(
      String bucketName,
      String objectName,
      InputStream stream,
      Long size,
      Map<String, String> headerMap,
      ServerSideEncryption sse,
      String contentType) {
    int current = 0;
    while (current < 3) {
      PutObjectOptions options = new PutObjectOptions(size, 0);
      options.setHeaders(headerMap);
      options.setSse(sse);
      options.setContentType(contentType);
      client.putObject(bucketName, objectName, stream, options);
      current++;
      return true;
    }
    return false;
  }

  /**
   * 上传文件
   *
   * @param bucketName bucket名称
   * @param objectName 文件名称
   * @param stream 文件流
   * @param size 大小
   * @param contentType 类型
   * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
   */
  public boolean putObject(
      String bucketName, String objectName, InputStream stream, long size, String contentType) {
    Map<String, String> headerMap = null;
    ServerSideEncryption sse = null;
    return this.putObjectWithRetry(
        bucketName, objectName, stream, size, headerMap, sse, contentType);
  }

  /**
   * 获取文件信息
   *
   * @param bucketName bucket名称
   * @param objectName 文件名称
   * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
   */
  @SneakyThrows(Exception.class)
  public ObjectStat getObjectInfo(String bucketName, String objectName) {
    return client.statObject(bucketName, objectName);
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
    client.removeObject(bucketName, objectName);
  }
}
