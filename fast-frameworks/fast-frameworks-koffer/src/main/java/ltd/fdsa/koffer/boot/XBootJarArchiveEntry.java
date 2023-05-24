package cn.zhumingwu.koffer.boot;

import cn.zhumingwu.koffer.XConstants;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.util.zip.ZipException;

/**
 * 为了兼容Spring-Boot FatJar 和普通Jar 的包内资源URL一致 所以去掉路径前面的 BOOT-INF/classes/
 */
public class XBootJarArchiveEntry extends JarArchiveEntry implements XConstants {

    public XBootJarArchiveEntry(ZipArchiveEntry entry) throws ZipException {
        super(entry);
    }

    @Override
    public String getName() {
        return super.getName().substring(BOOT_INF_CLASSES.length());
    }
}
