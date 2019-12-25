package ltd.fdsa.cloud.constant;

/**
 * @Classname SecretConstant
 * @Description jwt使用常量
 * @Date 2019/12/19 9:13
 * @Author 高进
 */
public class SecretConstant {
    /**
     * 签名密钥
     */
    public static final String BASE64_SECRET = "e10adc3949ba59abbe56e057f20f883e";
    /**
     * 超时毫秒数
     */
    public static final int EXPIRES_MILLISECOND = 30 * 60 * 1000;

    /**
     * 用于JWT加密的密钥
     */
    public static final String DATA_KEY = "u^3y6SPER41jm*fn";
}
