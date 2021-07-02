package ltd.fdsa.cloud.jwt.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
public class User implements Serializable {
    private static final long serialVersionUID = -5194986040454352027L;

    private static final String CLAIM_USER_ID = "id";
    private static final String CLAIM_USER_ROLES = "roles";

    private String id;
    private String roles;

    public static User generateUser(Map<String, Object> claims) {
        User user = new User();
        user.id = String.valueOf(claims.get(CLAIM_USER_ID));
        user.roles = String.valueOf(claims.get(CLAIM_USER_ROLES));
        return user;
    }

    public Map<String, Object> generateClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, this.id);
        claims.put(CLAIM_USER_ROLES, this.roles);
        return claims;
    }
}
