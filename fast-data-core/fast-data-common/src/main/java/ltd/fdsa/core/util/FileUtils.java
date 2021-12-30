package ltd.fdsa.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;


@Slf4j
public class FileUtils {

    private static final String ENCODING = "UTF-8";

    /**
     * 判断文件是否存在
     *
     * @param path  path
     * @return boolean 
     */
    public static boolean checkFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 根据路径读取文件
     *
     * @param filePath path
     * @return String
     */
    public static String readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        return readFile(file);
    }

    /**
     * 读取文件
     *
     * @param file path
     * @return String
     */
    public static String readFile(File file) {
        if (file == null) {
            return null;
        }
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try (FileInputStream in = new FileInputStream(file)) {
            in.read(fileContent);
        } catch (FileNotFoundException e) {
            log.error("FileUtils.readFile", e);
        } catch (IOException e) {
            log.error("FileUtils.readFile", e);
        }
        try {
            return new String(fileContent, ENCODING);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException", e);
            return null;
        }
    }

    /**
     * 写文件
     *
     * @param filePath path
     * @param content content
     */
    public static void writeFile(String filePath, String content) {
        FileWriter fw = null;
        try {
            File file = new File(filePath);
            //如果文件夹不存在，则创建文件夹
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //如果文件不存在，则创建文件,写入第一行内容
            if (!file.exists()) {
                file.createNewFile();
                fw = new FileWriter(file);
            } else {
                fw = new FileWriter(file, false);
            }
        } catch (IOException e) {
            log.error("FileUtils.writeFile", e);
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    log.error("FileUtils.writeFile", e);
                }
            }
        }

        try (PrintWriter pw = new PrintWriter(fw)) {
            pw.print(content);
            pw.flush();
        } catch (Exception e) {
            log.error("FileUtils.writeFile", e);
        }
    }
}
