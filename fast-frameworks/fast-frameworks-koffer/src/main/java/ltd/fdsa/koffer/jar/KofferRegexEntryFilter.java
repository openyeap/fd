package cn.zhumingwu.koffer.jar;

import cn.zhumingwu.koffer.XEntryFilter;
import cn.zhumingwu.koffer.filter.XRegexEntryFilter;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;

import java.util.regex.Pattern;

/**
 * Jar记录正则表达式过滤器
 */
public class KofferRegexEntryFilter extends XRegexEntryFilter<JarArchiveEntry>
        implements XEntryFilter<JarArchiveEntry> {

    public KofferRegexEntryFilter(String regex) {
        super(regex);
    }

    public KofferRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(JarArchiveEntry entry) {
        return entry.getName();
    }
}
