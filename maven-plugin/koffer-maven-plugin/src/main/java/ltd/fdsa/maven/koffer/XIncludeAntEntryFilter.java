package ltd.fdsa.maven.koffer;

import ltd.fdsa.koffer.XEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * 包含过滤器
 */
public class XIncludeAntEntryFilter extends XAntEntryFilter
        implements XEntryFilter<JarArchiveEntry> {

    public XIncludeAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    public boolean filtrate(JarArchiveEntry entry) {
        return matches(entry.getName());
    }
}
