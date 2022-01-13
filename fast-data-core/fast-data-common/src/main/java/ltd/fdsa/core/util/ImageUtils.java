package ltd.fdsa.core.util;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

import com.esotericsoftware.minlog.Log;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class ImageUtils {

    public static byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = new FileImageInputStream(new File(path));
            output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    // byte数组到图片
    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) {
            return;
        }
        FileImageOutputStream imageOutput = null;
        try {
            imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            log.info("Make Picture success,Please find image in {}", path);
        } catch (Exception ex) {
            log.error("Exception", ex);
        } finally {
            if (imageOutput != null) {
                try {
                    imageOutput.close();
                } catch (IOException e) {
                    log.error("Exception", e);
                }
            }
        }
    }

    /**
     * 将图片写入到磁盘
     *
     * @param img      图片数据流
     * @param fileName 文件保存时的名称
     */
    public static void writeImageToDisk(byte[] img, String fileName) {
        FileOutputStream fops = null;
        try {
            File file = new File(fileName);
            fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
            log.info("图片已经写入到本地，文件名:{}", fileName);
        } catch (Exception e) {
            log.error("Exception", e);
        } finally {
            if (fops != null) {
                try {
                    fops.close();
                } catch (IOException e) {
                    log.error("Exception", e);
                }
            }

        }
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return byte[]
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return null;
    }

    /**
     * 将图片从网上下载下来保存到磁盘
     *
     * @param strUrl   网络图片地址
     * @param fileName 本地图片全路径
     */
    public static void writePicToDiskFromWeb(String strUrl, String fileName) {
        writeImageToDisk(getImageFromNetByUrl(strUrl), fileName);
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return byte[]
     * @throws Exception 错误
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
