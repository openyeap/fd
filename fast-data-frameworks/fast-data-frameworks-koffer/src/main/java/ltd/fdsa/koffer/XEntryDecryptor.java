package ltd.fdsa.koffer;

/**
 * 记录可过滤的解密器
 */
public abstract class XEntryDecryptor<E> extends XWrappedDecryptor
        implements XDecryptor, XEntryFilter<E> {
    protected final XEntryFilter<E> filter;
    protected final XNopDecryptor xNopDecryptor = new XNopDecryptor();

    protected XEntryDecryptor(XDecryptor xDecryptor) {
        this(xDecryptor, null);
    }

    protected XEntryDecryptor(XDecryptor xDecryptor, XEntryFilter<E> filter) {
        super(xDecryptor);
        this.filter = filter;
    }

    @Override
    public boolean filtrate(E entry) {
        return filter == null || filter.filtrate(entry);
    }
}
