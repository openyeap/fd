package ltd.fdsa.koffer.dir;

import ltd.fdsa.koffer.XEntryFilter;
import ltd.fdsa.koffer.filter.XAntEntryFilter;

import java.io.File;

/**
 * 文件记录Ant表达式过滤器
 */
public class XDirAntEntryFilter extends XAntEntryFilter<File> implements XEntryFilter<File> {

    public XDirAntEntryFilter(String ant) {
        super(ant);
    }

    @Override
    protected String toText(File entry) {
        return entry.getName();
    }
}
