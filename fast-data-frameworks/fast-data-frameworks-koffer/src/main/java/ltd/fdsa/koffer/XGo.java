package ltd.fdsa.koffer;

import ltd.fdsa.koffer.key.XKey;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Koffer GoLang 启动器
 */
public class XGo {
    private static final String CLRF = System.getProperty("line.separator");

    private static void copyFile(File file, String fileName) throws IOException {
        InputStream initialStream = XGo.class.getClassLoader().getResourceAsStream(fileName);
        String dir = file.getParent();
        File targetFile = new File(dir, fileName);
        java.nio.file.Files.copy(
                initialStream,
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(initialStream);

//        try (InputStream in = XGo.class.getClassLoader().getResourceAsStream(fileName)) {
//            byte[] buffer = new byte[in.available()];
//            in.read(buffer);
//            String dir = file.getParent();
//            File src = new File(dir, fileName);
//            try (OutputStream out = new FileOutputStream(src)) {
//                out.write(buffer);
//                out.flush();
//            }
//        }
    }

    public static void make(File file, XKey xKey) throws IOException {
        if (1 == 1) {
            copyFile(file, "koffer.exe");
            copyFile(file, "koffer");
            return;
        }
        byte[] md5 = XKit.md5(file);
        byte[] sha1 = XKit.sha1(file);

        byte[] algorithm = xKey.getAlgorithm().getBytes(StandardCharsets.UTF_8);
        byte[] keysize = String.valueOf(xKey.getKeysize()).getBytes(StandardCharsets.UTF_8);
        byte[] ivsize = String.valueOf(xKey.getIvsize()).getBytes(StandardCharsets.UTF_8);
        byte[] password = xKey.getPassword().getBytes(StandardCharsets.UTF_8);

        Map<String, String> variables = new HashMap<>();
        variables.put("koffer.md5", convert(md5));
        variables.put("koffer.sha1", convert(sha1));
        variables.put("xKey.algorithm", convert(algorithm));
        variables.put("xKey.keysize", convert(keysize));
        variables.put("xKey.ivsize", convert(ivsize));
        variables.put("xKey.password", convert(password));

        URL url = XGo.class.getClassLoader().getResource("koffer.go");
        if (url == null) {
            throw new IOException("IOException：could not find koffer.go");
        }
        String dir = file.getParent();
        File src = new File(dir, "koffer.go");
        try (InputStream in = url.openStream();
             Reader reader = new InputStreamReader(in);
             BufferedReader br = new BufferedReader(reader);
             OutputStream out = new FileOutputStream(src);
             Writer writer = new OutputStreamWriter(out);
             BufferedWriter bw = new BufferedWriter(writer)) {
            String line;
            while ((line = br.readLine()) != null) {
                for (Map.Entry<String, String> variable : variables.entrySet()) {
                    line = line.replace("#{" + variable.getKey() + "}", variable.getValue());
                }
                bw.write(line);
                bw.write(CLRF);
            }
            bw.flush();
            writer.flush();
            out.flush();
        }

    }


    private static String convert(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(b & 0xFF);
        }
        return builder.toString();
    }
}
