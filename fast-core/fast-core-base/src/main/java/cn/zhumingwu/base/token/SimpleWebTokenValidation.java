package cn.zhumingwu.base.token;

import java.util.Map;

public interface SimpleWebTokenValidation {

    public enum ValidationResult {
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

        private Map<String, String> claims;

        public static ValidationResult valid(Map<String, String> claims) {
            var result = ValidationResult.TOKEN_VALID;
            result.claims = claims;
            return result;
        }

        public Map<String, String> getClaims() {
            return claims;
        }
    }
    public ValidationResult validate(String token);
}
