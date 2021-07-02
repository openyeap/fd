package ltd.fdsa.koffer.jar;

import ltd.fdsa.koffer.XEntryFilter;
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
