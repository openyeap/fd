import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ClassLoader extends URLClassLoader {

    public ClassLoader(String... paths) {
        this(getURLs(paths), ClassLoader.class.getClassLoader());
    }

    public ClassLoader(URL[] urls, java.lang.ClassLoader parent) {
        super(urls, parent);
    }

    public List<Class<?>> loadClasses(JarEntryFilter entryFilter, ClassFilter classFilter) {
        List<Class<?>> results = new ArrayList<Class<?>>();
        for (var url : this.getURLs()) {
            var file = url.getFile();
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(file);
            } catch (IOException e) {
                continue;
            }
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                if (entryFilter.accept(entry)) {
                    var path = entry.getName();
                    path = path.substring(0, path.lastIndexOf("."));
                    path = path.replace("/", ".");
                    Class<?> obj = null;
                    try {
                        obj = this.loadClass(path);
                    } catch (ClassNotFoundException e) {
                        continue;
                    }
                    if (classFilter.accept(obj)) {
                        results.add(obj);
                    }
                }
            }

        }
        return results;
    }

    public Field[] getDeclaredFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>(16);
        while (clazz != null) {
            var fields = clazz.getDeclaredFields();
            var sss = Stream.of(fields).collect(Collectors.toList());
            fieldList.addAll(sss);
            clazz = clazz.getSuperclass();
        }
        Field[] f = new Field[fieldList.size()];
        return fieldList.toArray(f);
    }

    private static URL[] getURLs(String[] paths) {
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
        if (null == path || Strings.isNullOrEmpty(path)) {
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
        Assert.isTrue(!Strings.isNullOrEmpty(path), "jar包路径不能为空.");

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


    @FunctionalInterface
    public interface ClassFilter {

        /**
         * Tests whether or not the specified abstract pathname should be
         * included in a pathname list.
         *
         * @param clazz The abstract pathname to be tested
         * @return <code>true</code> if and only if <code>pathname</code>
         * should be included
         */
        boolean accept(Class clazz);
    }

    @FunctionalInterface
    public interface JarEntryFilter {

        /**
         * Tests whether or not the specified abstract pathname should be
         * included in a pathname list.
         *
         * @param entry The abstract pathname to be tested
         * @return <code>true</code> if and only if <code>pathname</code>
         * should be included
         */
        boolean accept(JarEntry entry);
    }
}