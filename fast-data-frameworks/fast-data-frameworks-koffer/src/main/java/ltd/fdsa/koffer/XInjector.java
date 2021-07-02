package ltd.fdsa.koffer;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Koffer API
 */
public class XInjector {

    /**
     * 往JAR包中注入Koffer框架的classes
     *
     * @param zos jar包输出流
     * @throws IOException I/O 异常
     */
    public static void inject(JarArchiveOutputStream zos) throws IOException {
        Set<String> directories = new HashSet<>();
        Enumeration<Resource> resources = Loaders.ant().load("ltd/fdsa/koffer/**");
        while (resources.hasMoreElements()) {
            Resource resource = resources.nextElement();
            String name = resource.getName();
            String directory = name.substring(0, name.lastIndexOf('/') + 1);
            if (directories.add(directory)) {
                JarArchiveEntry xDirEntry = new JarArchiveEntry(directory);
                xDirEntry.setTime(System.currentTimeMillis());
                zos.putArchiveEntry(xDirEntry);
                zos.closeArchiveEntry();
            }
            JarArchiveEntry KofferEntry = new JarArchiveEntry(name);
            KofferEntry.setTime(System.currentTimeMillis());
            zos.putArchiveEntry(KofferEntry);
            try (InputStream ris = resource.getInputStream()) {
                XKit.transfer(ris, zos);
            }
            zos.closeArchiveEntry();
        }
    }
}
