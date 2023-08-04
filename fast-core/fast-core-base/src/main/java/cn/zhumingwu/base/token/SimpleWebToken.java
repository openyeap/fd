package cn.zhumingwu.base.token;

import lombok.Data;

import java.util.*;

@Data
public class SimpleWebToken {
    private Integer expiredIn;
    private String accessToken;
    private String refreshToken;
    private Map<String, String> claims;

    public String getUserId() {
        return this.claims.get("u");
    }

    public String getCorpId() {
        return this.claims.get("c");
    }
    public String[] getRoles() {
        return this.claims.get("r").split(",");
    }
}
