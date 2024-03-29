package cn.zhumingwu.koffer.jar;

import cn.zhumingwu.koffer.XEntryFilter;
import cn.zhumingwu.koffer.filter.XAntEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

/**
 * Jar记录Ant表达式过滤器
 */
public class KofferAntEntryFilter extends XAntEntryFilter<JarArchiveEntry>
        implements XEntryFilter<JarArchiveEntry> {

    public KofferAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(JarArchiveEntry entry) {
        return entry.getName();
    }
}
