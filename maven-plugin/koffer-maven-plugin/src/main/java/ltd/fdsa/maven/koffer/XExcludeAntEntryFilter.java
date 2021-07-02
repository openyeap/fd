package ltd.fdsa.maven.koffer;

import ltd.fdsa.koffer.XEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * 排除过滤器
 */
public class XExcludeAntEntryFilter extends XAntEntryFilter
        implements XEntryFilter<JarArchiveEntry> {

    public XExcludeAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    public boolean filtrate(JarArchiveEntry entry) {
        return !matches(entry.getName());
    }
}
