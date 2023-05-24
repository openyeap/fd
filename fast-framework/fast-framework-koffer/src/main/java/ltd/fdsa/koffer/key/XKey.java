package cn.zhumingwu.koffer.key;

/**
 * 密钥
 */
public interface XKey {

    /**
     * @return 密钥算法名称
     */
    String getAlgorithm();

    /**
     * @return 密钥长度
     */
    int getKeysize();

    /**
     * @return 向量长度
     */
    int getIvsize();

    /**
     * @return 密码
     */
    String getPassword();

    /**
     * @return 加密密钥
     */
    byte[] getEncryptKey();

    /**
     * @return 解密密钥
     */
    byte[] getDecryptKey();

    /**
     * @return 向量参数
     */
    byte[] getIvParameter();
}
