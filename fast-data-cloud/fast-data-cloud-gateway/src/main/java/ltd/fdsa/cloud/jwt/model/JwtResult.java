package ltd.fdsa.cloud.jwt.model;

import lombok.Data;


@Data
public class JwtResult {
    private String accessToken;
    private String refreshToken;
    private Integer expiredIn;
}
