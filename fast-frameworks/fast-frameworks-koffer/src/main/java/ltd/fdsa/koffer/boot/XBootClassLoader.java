package cn.zhumingwu.koffer.boot;

import cn.zhumingwu.koffer.XDecryptor;
import cn.zhumingwu.koffer.XEncryptor;
import cn.zhumingwu.koffer.XKit;
import cn.zhumingwu.koffer.key.XKey;
import cn.zhumingwu.koffer.reflection.KReflection;
import org.springframework.boot.loader.LaunchedURLClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Enumeration;

/**
 * X类加载器
 */
public class XBootClassLoader extends LaunchedURLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    private final XBootURLHandler xBootURLHandler;
    private final Object urlClassPath;
    private final Method getResource;
    private final Method getCodeSourceURL;
    private final Method getCodeSigners;

    public XBootClassLoader(
            URL[] urls, ClassLoader parent, XDecryptor xDecryptor, XEncryptor xEncryptor, XKey xKey)
            throws Exception {
        super(urls, parent);
        this.xBootURLHandler = new XBootURLHandler(xDecryptor, xEncryptor, xKey, this);
        this.urlClassPath = KReflection.field(URLClassLoader.class, "ucp").get(this).value();
        this.getResource =
                KReflection.method(urlClassPath.getClass(), "getResource", String.class).method();
        this.getCodeSourceURL =
                KReflection.method(getResource.getReturnType(), "getCodeSourceURL").method();
        this.getCodeSigners =
                KReflection.method(getResource.getReturnType(), "getCodeSigners").method();
    }

    @Override
    public URL findResource(String name) {
        URL url = super.findResource(name);
        if (url == null) {
            return null;
        }
        try {
            return new URL(
                    url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xBootURLHandler);
        } catch (MalformedURLException e) {
            return url;
        }
    }

    @Override
    public Enumeration<URL> findResources(String name) throws IOException {
        Enumeration<URL> enumeration = super.findResources(name);
        if (enumeration == null) {
            return null;
        }
        return new XBootEnumeration(enumeration);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassFormatError e) {
            String path = name.replace('.', '/').concat(".class");
            URL url = findResource(path);
            if (url == null) {
                throw new ClassNotFoundException(name, e);
            }
            try (InputStream in = url.openStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                XKit.transfer(in, bos);
                byte[] bytes = bos.toByteArray();
                Object resource = getResource.invoke(urlClassPath, path);
                URL codeSourceURL = (URL) getCodeSourceURL.invoke(resource);
                CodeSigner[] codeSigners = (CodeSigner[]) getCodeSigners.invoke(resource);
                CodeSource codeSource = new CodeSource(codeSourceURL, codeSigners);
                return defineClass(name, bytes, 0, bytes.length, codeSource);
            } catch (Throwable t) {
                throw new ClassNotFoundException(name, t);
            }
        }
    }

    private class XBootEnumeration implements Enumeration<URL> {
        private final Enumeration<URL> enumeration;

        XBootEnumeration(Enumeration<URL> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasMoreElements() {
            return enumeration.hasMoreElements();
        }

        @Override
        public URL nextElement() {
            URL url = enumeration.nextElement();
            if (url == null) {
                return null;
            }
            try {
                return new URL(
                        url.getProtocol(), url.getHost(), url.getPort(), url.getFile(), xBootURLHandler);
            } catch (MalformedURLException e) {
                return url;
            }
        }
    }
}
