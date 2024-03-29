package cn.zhumingwu.koffer;

/**
 * 记录可过滤的加密器
 */
public abstract class XEntryEncryptor<E> extends XWrappedEncryptor
        implements XEncryptor, XEntryFilter<E> {
    protected final XEntryFilter<E> filter;
    protected final XNopEncryptor xNopEncryptor = new XNopEncryptor();

    protected XEntryEncryptor(XEncryptor xEncryptor) {
        this(xEncryptor, null);
    }

    protected XEntryEncryptor(XEncryptor xEncryptor, XEntryFilter<E> filter) {
        super(xEncryptor);
        this.filter = filter;
    }

    @Override
    public boolean filtrate(E entry) {
        return filter == null || filter.filtrate(entry);
    }
}
