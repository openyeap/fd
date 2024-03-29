package cn.zhumingwu.koffer.zip;

import cn.zhumingwu.koffer.XEntryFilter;
import cn.zhumingwu.koffer.filter.XRegexEntryFilter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import java.util.regex.Pattern;

/**
 * Zip记录正则表达式过滤器
 */
public class XZipRegexEntryFilter extends XRegexEntryFilter<ZipArchiveEntry>
        implements XEntryFilter<ZipArchiveEntry> {

    public XZipRegexEntryFilter(String regex) {
        super(regex);
    }

    public XZipRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(ZipArchiveEntry entry) {
        return entry.getName();
    }
}
