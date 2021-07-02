package ltd.fdsa.koffer.jar;

import ltd.fdsa.koffer.XConstants;
import ltd.fdsa.koffer.XLauncher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * JAR包启动器
 */
public class KofferLauncher implements XConstants {
    private final XLauncher xLauncher;

    public KofferLauncher(String... args) throws Exception {
        this.xLauncher = new XLauncher(args);
    }

    public static void main(String... args) throws Exception {
        new KofferLauncher(args).launch();
    }

    public void launch() throws Exception {
        KofferClassLoader KofferClassLoader;

        ClassLoader classLoader = this.getClass().getClassLoader();
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
            KofferClassLoader =
                    new KofferClassLoader(
                            urlClassLoader.getURLs(),
                            classLoader.getParent(),
                            xLauncher.xDecryptor,
                            xLauncher.xEncryptor,
                            xLauncher.xKey);
        } else {
            ProtectionDomain domain = this.getClass().getProtectionDomain();
            CodeSource source = domain.getCodeSource();
            URI location = (source == null ? null : source.getLocation().toURI());
            String path = (location == null ? null : location.getSchemeSpecificPart());
            if (path == null) {
                throw new IllegalStateException("Unable to determine code source archive");
            }
            File jar = new File(path);
            URL url = jar.toURI().toURL();
            KofferClassLoader =
                    new KofferClassLoader(
                            new URL[]{url},
                            classLoader.getParent(),
                            xLauncher.xDecryptor,
                            xLauncher.xEncryptor,
                            xLauncher.xKey);
        }

        Thread.currentThread().setContextClassLoader(KofferClassLoader);
        ProtectionDomain domain = this.getClass().getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URI location = source.getLocation().toURI();
        String filepath = location.getSchemeSpecificPart();
        File file = new File(filepath);
        JarFile jar = new JarFile(file, false);
        Manifest manifest = jar.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        String jarMainClass = attributes.getValue("Jar-Main-Class");
        Class<?> mainClass = KofferClassLoader.loadClass(jarMainClass);
        Method mainMethod = mainClass.getMethod("main", String[].class);
        mainMethod.invoke(null, new Object[]{xLauncher.args});
    }
}
