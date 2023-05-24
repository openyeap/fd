package cn.zhumingwu.koffer.jar;

import cn.zhumingwu.koffer.XEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Spring-Boot 所有资源加密过滤器
 */
public class KofferAllEntryFilter implements XEntryFilter<JarArchiveEntry> {

    @Override
    public boolean filtrate(JarArchiveEntry entry) {
        return true;
    }
}
