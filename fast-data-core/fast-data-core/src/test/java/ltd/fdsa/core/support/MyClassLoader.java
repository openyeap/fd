package ltd.fdsa.core.support;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class MyClassLoader extends ClassLoader {
    private final Set<String> classNameSet;

    public MyClassLoader(ClassLoader parent) {
        super(parent);
        this.classNameSet = new HashSet<>();
    }

    public void addClassName(String... classNames) {

        for (int i = 0; i < classNames.length; i++) {
            String className = classNames[i];
            this.classNameSet.add(className);
        }
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (!this.classNameSet.contains(name)) {
            return super.loadClass(name);
        }

        try {
            String url = "file:" + System.getProperty("user.dir").replace("\\", "/") + "/target/classes/" + name.replace(".", "/") + ".class";
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();
            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }
            input.close();
            byte[] classData = buffer.toByteArray();
            return defineClass(name, classData, 0, classData.length);

        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        return super.loadClass(name);
    }
}
