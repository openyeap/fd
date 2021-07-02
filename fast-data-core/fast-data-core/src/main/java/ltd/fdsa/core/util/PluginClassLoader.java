package ltd.fdsa.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(String... paths) {
        this(getURLs(paths), PluginClassLoader.class.getClassLoader());
    }

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    private static URL[] getURLs(String[] paths) {
        Assert.isNull(null != paths && 0 != paths.length, "jar包路径不能为空.");

        List<String> dirs = new ArrayList<String>();
        for (String path : paths) {
            dirs.add(path);
            collectDirs(path, dirs);
        }

        List<URL> urls = new ArrayList<URL>();
        for (String path : dirs) {
            urls.addAll(doGetURLs(path));
        }

        return urls.toArray(new URL[0]);
    }

    private static void collectDirs(String path, List<String> collector) {
        if (null == path || Strings.isBlank(path)) {
            return;
        }

        File current = new File(path);
        if (!current.exists() || !current.isDirectory()) {
            return;
        }

        for (File child : current.listFiles()) {
            if (!child.isDirectory()) {
                continue;
            }

            collector.add(child.getAbsolutePath());
            collectDirs(child.getAbsolutePath(), collector);
        }
    }

    private static List<URL> doGetURLs(final String path) {
        Assert.isTrue(!Strings.isBlank(path), "jar包路径不能为空.");

        File jarPath = new File(path);

        Assert.isTrue(jarPath.exists() && jarPath.isDirectory(), "jar包路径必须存在且为目录.");

        /* set filter */
        FileFilter jarFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        };

        /* iterate all jar */
        File[] allJars = new File(path).listFiles(jarFilter);
        List<URL> jarURLs = new ArrayList<URL>(allJars.length);

        for (int i = 0; i < allJars.length; i++) {
            try {
                jarURLs.add(allJars[i].toURI().toURL());
            } catch (Exception e) {
                log.error("系统加载jar包出错", e);
            }
        }

        return jarURLs;
    }


//    private URLClassLoader classLoader;

//    public void addToClassLoader(String baseDir, FileFilter filter, boolean quiet) {
//        File base = new File(baseDir);
//        if (base != null && base.exists() && base.isDirectory()) {
//            File[] files = base.listFiles(filter);
//            if (files == null || files.length == 0) {
//                if (!quiet)
//                    log.error("No files added to classloader from lib:{} resolved as:{}", baseDir, base.getAbsolutePath());
//            } else {
//                this.classLoader = replaceClassLoader(this.classLoader, base, filter);
//            }
//        } else if (!quiet) {
//            log.error("No files added to classloader from lib:{} resolved as:{}", baseDir, base.getAbsolutePath());
//        }
//    }
//
//    private URLClassLoader replaceClassLoader(             URLClassLoader oldLoader, File base, FileFilter filter) {
//        if (null != base && base.canRead() && base.isDirectory()) {
//            File[] files = base.listFiles(filter);
//            if (null == files || 0 == files.length) {
//                log.error("replaceClassLoader base dir:{} is empty", base.getAbsolutePath());
//                return oldLoader;
//            }
//            log.error("replaceClassLoader base dir: {} ,size: {}", base.getAbsolutePath(), Integer.valueOf(files.length));
//            URL[] oldElements = oldLoader.getURLs();
//            URL[] elements = new URL[oldElements.length + files.length];
//            System.arraycopy(oldElements, 0, elements, 0, oldElements.length);
//            for (int j = 0; j < files.length; j++) {
//                try {
//                    URL element = files[j].toURI().normalize().toURL();
//                    elements[oldElements.length + j] = element;
//                    NamingUtils.formatLog(log, "Adding '{}' to classloader", element.toString());
//                } catch (MalformedURLException e) {
//                    log.error("load jar file error", e);
//                }
//            }
//            ClassLoader oldParent = oldLoader.getParent();
//            return URLClassLoader.newInstance(elements, oldParent);
//        }
//        return oldLoader;
//    }
//
//    private URLClassLoader createClassLoader(File libDir, ClassLoader parent) {
//        if (null == parent) parent = Thread.currentThread().getContextClassLoader();
//        return replaceClassLoader(URLClassLoader.newInstance(new URL[0], parent), libDir, null);
//    }
//
//    public Class<?> loadClass(String className) throws ClassNotFoundException {
//        return this.classLoader.loadClass(className);
//    }
}
