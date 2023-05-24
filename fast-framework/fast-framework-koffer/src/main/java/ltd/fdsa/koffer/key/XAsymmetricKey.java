package cn.zhumingwu.koffer.key;

/**
 * 非对称密钥
 */
public interface XAsymmetricKey extends XKey {

    /**
     * @return 公钥
     */
    byte[] getPublicKey();

    /**
     * @return 私钥
     */
    byte[] getPrivateKey();
}
