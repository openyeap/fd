package cn.zhumingwu.koffer.filter;

import cn.zhumingwu.koffer.XEntryFilter;

/**
 * 非门逻辑过滤器
 */
public class XNotEntryFilter<E> implements XEntryFilter<E> {
    private final XEntryFilter<E> delegate;

    public XNotEntryFilter(XEntryFilter<E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean filtrate(E entry) {
        return !delegate.filtrate(entry);
    }
}
