package cn.zhumingwu.koffer.zip;

import cn.zhumingwu.koffer.XEntryFilter;
import cn.zhumingwu.koffer.filter.XAntEntryFilter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

/**
 * Zip记录Ant表达式过滤器
 */
public class XZipAntEntryFilter extends XAntEntryFilter<ZipArchiveEntry>
        implements XEntryFilter<ZipArchiveEntry> {

    public XZipAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(ZipArchiveEntry entry) {
        return entry.getName();
    }
}
