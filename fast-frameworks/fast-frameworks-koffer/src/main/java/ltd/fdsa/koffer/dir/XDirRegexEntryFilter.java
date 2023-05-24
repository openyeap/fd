package cn.zhumingwu.koffer.dir;

import cn.zhumingwu.koffer.XEntryFilter;
import cn.zhumingwu.koffer.filter.XRegexEntryFilter;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 文件记录正则表达式过滤器
 */
public class XDirRegexEntryFilter extends XRegexEntryFilter<File> implements XEntryFilter<File> {

    public XDirRegexEntryFilter(String regex) {
        super(regex);
    }

    public XDirRegexEntryFilter(Pattern pattern) {
        super(pattern);
    }

    @Override
    protected String toText(File entry) {
        return entry.getName();
    }
}
