package ltd.fdsa.cloud.jwt.model;


public enum JwtValidationResultType {
    /**
     * 验证
     */
    TOKEN_VALID,
    /**
     * 过期
     */
    TOKEN_EXPIRED,
    /**
     * 签名错误
     */
    TOKEN_INVALID_SIGNATURE,
    /**
     * 处理错误
     */
    TOKEN_PROCESS_ERROR,
    /**
     * 无效
     */
    TOKEN_NOT_PRESENT;
}
