package ltd.fdsa.koffer.key;

/**
 * 对称密钥
 */
public interface XSymmetricKey extends XKey {

    /**
     * @return 密钥
     */
    byte[] getSecretKey();
}
