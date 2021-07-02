package ltd.fdsa.koffer.jar;

import ltd.fdsa.koffer.XConstants;
import ltd.fdsa.koffer.XDecryptor;
import ltd.fdsa.koffer.XEncryptor;
import ltd.fdsa.koffer.key.XKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 加密的URL处理器
 */
public class KofferURLHandler extends URLStreamHandler implements XConstants {
    private final XDecryptor xDecryptor;
    private final XEncryptor xEncryptor;
    private final XKey xKey;
    private final Set<String> indexes;

    public KofferURLHandler(
            XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey, ClassLoader classLoader)
            throws Exception {
        this.xDecryptor = xDecryptor;
        this.xEncryptor = xEncryptor;
        this.xKey = xKey;
        this.indexes = new LinkedHashSet<>();
        Enumeration<URL> resources = classLoader.getResources(Koffer_INF_DIR + Koffer_INF_IDX);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String url = resource.toString();
            String classpath = url.substring(0, url.lastIndexOf("!/") + 2);
            InputStream in = resource.openStream();
            InputStreamReader isr = new InputStreamReader(in);
            LineNumberReader lnr = new LineNumberReader(isr);
            String name;
            while ((name = lnr.readLine()) != null) indexes.add(classpath + name);
        }
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        URLConnection urlConnection = new URL(url.toString()).openConnection();
        return indexes.contains(url.toString()) && urlConnection instanceof JarURLConnection
                ? new KofferURLConnection((JarURLConnection) urlConnection, xDecryptor, xEncryptor, xKey)
                : urlConnection;
    }
}
