package cn.zhumingwu.data.hub.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileUtils {

    private static final String ENCODING = "UTF-8";

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean checkFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 根据路径读取文件
     *
     * @param filePath
     * @return
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
     * @param file
     * @return
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, ENCODING);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + ENCODING);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 写文件
     *
     * @param filePath
     * @param content
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
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try (PrintWriter pw = new PrintWriter(fw)) {
            pw.print(content);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete recursively
     *
     * @param root
     * @return
     */
    public static boolean deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File child : children) {
                        deleteRecursively(child);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }

    public static void deleteFile(String fileName) {
        // file
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void writeFileContent(File file, byte[] data) {

        // file
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        // append file content
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
    }

    public static byte[] readFileContent(File file) {
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
        return filecontent;
    }

}
