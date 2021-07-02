package ltd.fdsa.koffer;

/**
 * 记录加/解密过滤器
 */
public interface XEntryFilter<E> {

    /**
     * 记录是否需要加/解密
     *
     * @param entry 记录
     * @return true: 需要 false:不需要
     */
    boolean filtrate(E entry);
}
