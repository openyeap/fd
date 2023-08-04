package cn.zhumingwu.base.token;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public class SimpleWebTokenGeneratorImpl implements SimpleWebTokenGenerator {
    public SimpleWebTokenGeneratorImpl(Integer expiredIn, String privateKey) {
        this.expiredIn = expiredIn;
        this.privateKey = privateKey;
    }

    private final Integer expiredIn;
    private final String privateKey;

    public static Builder builder() {
        return new Builder();
    }


    public SimpleWebToken generate(String userId, String clientId, Map<String, String> claims, String... roles) {
        var b = new Builder().expiredIn(this.expiredIn).uid(userId).cid(clientId).roles(roles).claim(claims);
        return b.signWith(this.privateKey);
    }

    public SimpleWebToken refresh(String refreshToken) {
        var b = new Builder().expiredIn(10);
        return b.signWith(this.privateKey);
    }

    public static class Builder {
        private Integer expiredIn;
        private final Map<String, String> claims;

        Builder() {
            this.claims = new LinkedHashMap<>(10);
        }

        public Builder expiredIn(Integer expiredIn) {
            this.expiredIn = expiredIn;
            return this;
        }

        public Builder claim(String key, String value) {
            this.claims.put(key, value);
            return this;
        }

        public Builder claim(Map<String, String> claims) {
            this.claims.putAll(claims);
            return this;
        }

        public Builder uid(String value) {
            this.claims.put("u", value);
            return this;
        }

        public Builder cid(String value) {
            this.claims.put("c", value);
            return this;
        }

        public Builder roles(String... values) {
            HashSet<String> set = new HashSet<String>();
            var roles = this.claims.get("r");
            if (!roles.isEmpty()) {
                Collections.addAll(set, roles.split(","));
            }
            Collections.addAll(set, values);
            this.claims.put("r", String.join(",", set));
            return this;
        }

        public SimpleWebToken signWith(String PRIVATE_KEY) {
            var expiredStamp = Instant.now().plusSeconds(this.expiredIn);
            var header = expiredStamp.getEpochSecond() & 0b11100000_00000000_00000000_00000000_00000000_00000000_00000000_00000000L;
            var body = new StringBuilder();
            body.append(header);
            body.append(".");
            for (var entry : this.claims.entrySet()) {
                body.append(entry.getKey());
                body.append(":");
                body.append(entry.getValue());
                body.append(";");
            }
            var accessToken = header + "." + Base64.getEncoder().encodeToString(body.toString().getBytes(StandardCharsets.UTF_8));
            var footer = "RSA";
            var refreshToken = "todo加密";
            var result = new SimpleWebToken();
            result.setAccessToken(accessToken);
            result.setExpiredIn(expiredIn);
            result.setRefreshToken(refreshToken);
            result.setClaims(this.claims);

            return result;
        }

    }
}
