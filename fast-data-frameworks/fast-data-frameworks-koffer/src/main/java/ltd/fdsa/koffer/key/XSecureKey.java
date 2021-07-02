package ltd.fdsa.koffer.key;

import java.io.Serializable;

/**
 * 密钥
 */
public abstract class XSecureKey implements XKey, Serializable {
    private static final long serialVersionUID = -5577962754674149355L;

    protected final String algorithm;
    protected final int keysize;
    protected final int ivsize;
    protected final String password;

    protected XSecureKey(String algorithm, int keysize, int ivsize, String password) {
        this.algorithm = algorithm;
        this.keysize = keysize;
        this.ivsize = ivsize;
        this.password = password;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public int getKeysize() {
        return keysize;
    }

    @Override
    public int getIvsize() {
        return ivsize;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
