package cn.zhumingwu.base.token;

import java.util.*;

public class SimpleWebTokenValidationImpl implements SimpleWebTokenValidation {
    public SimpleWebTokenValidationImpl(Integer expiredIn, String publicKey) {
        this.expiredIn = expiredIn;
        this.publicKey = publicKey;

    }

    private final Integer expiredIn;
    private final String publicKey;

    public static Builder builder() {
        return new Builder();
    }


    @Override
    public ValidationResult validate(String token) {
        return new Builder().validate(token, "public key");
    }


    public static class Builder {
        private final Map<String, String> claims;

        Builder() {
            this.claims = new LinkedHashMap<>(10);
        }
        public ValidationResult validate(String token, String publicKey) {
            try {
                var tokens = token.split("\\.");
                if (tokens.length != 3) {
                    return ValidationResult.TOKEN_NOT_PRESENT;
                }
                var header = Long.getLong(tokens[0]);
                if ((header & 0b11100000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L) != 0b11100000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L) {
                    return ValidationResult.TOKEN_NOT_PRESENT;
                }
                var expiredStamp = header & (0b00011111_11111111_11111111_11111111_11111111_11111111_11111111_11111111L);
                if (System.currentTimeMillis() / 1000 < expiredStamp) {
                    return ValidationResult.TOKEN_EXPIRED;
                }
                var body = tokens[1];
//todo sign
                if (body.length() == 0) {
                    return ValidationResult.TOKEN_INVALID_SIGNATURE;
                }
                for (var entry : body.split(";")) {
                    var kv = entry.split(":");
                    this.claims.put(kv[0], kv[1]);
                }
                return ValidationResult.valid(this.claims);
            } catch (Exception ex) {
                return ValidationResult.TOKEN_PROCESS_ERROR;
            }
        }
    }
}
