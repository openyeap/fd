package ltd.fdsa.cloud.jwt.impl;

import io.jsonwebtoken.*;
import ltd.fdsa.cloud.jwt.IJwtToken;
import ltd.fdsa.cloud.jwt.JwtProperties;
import ltd.fdsa.cloud.jwt.constant.JwtConstant;
import ltd.fdsa.cloud.jwt.model.JwtResult;
import ltd.fdsa.cloud.jwt.model.JwtValidationResult;
import ltd.fdsa.cloud.jwt.model.JwtValidationResultType;
import ltd.fdsa.cloud.jwt.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.UUID;


public class JwtTokenImpl implements IJwtToken, InitializingBean {
    private final String PRIVATE_KEY;

    private final String PUBLIC_KEY;

    private final int EXPIRED_MINUTE;

    private final int REFRESH_MINUTE;

    public JwtTokenImpl(JwtProperties properties) {
        this.PRIVATE_KEY = properties.getPrivateKey();
        this.PUBLIC_KEY = properties.getPublicKey();
        this.EXPIRED_MINUTE = properties.getAccess_token_expiration_minute();
        this.REFRESH_MINUTE = properties.getRefresh_token_expiration_minute();
    }

    @Override
    public JwtResult generate(User user) {
        JwtResult result = new JwtResult();
        result.setExpiredIn(EXPIRED_MINUTE);
        result.setAccessToken(generateTokenString(user.getId(), EXPIRED_MINUTE, user.generateClaims()));
        result.setRefreshToken(generateTokenString(user.getId(), REFRESH_MINUTE, user.generateClaims()));
        return result;
    }

    @Override
    public JwtResult refreshGenerate(String oldRefreshToken) {

        User user = User.generateUser(Jwts.parser().setSigningKey(PUBLIC_KEY).parseClaimsJws(oldRefreshToken).getBody());
        if (user == null) {
            return null;
        }
        return this.generate(user);
    }

    @Override
    public JwtValidationResult validate(String token) {
        JwtValidationResult validationResult = new JwtValidationResult();
        try {
            if (StringUtils.isEmpty(token)) {
                validationResult.setResultType(JwtValidationResultType.TOKEN_INVALID_SIGNATURE);
                return validationResult;
            }
            validationResult.setUser(User.generateUser(Jwts.parser().setSigningKey(PUBLIC_KEY).parseClaimsJws(token).getBody()));
            validationResult.setResultType(JwtValidationResultType.TOKEN_VALID);
        } catch (ExpiredJwtException e) {
            validationResult.setResultType(JwtValidationResultType.TOKEN_EXPIRED);
        } catch (SignatureException e) {
            validationResult.setResultType(JwtValidationResultType.TOKEN_INVALID_SIGNATURE);
        } catch (Throwable t) {
            validationResult.setResultType(JwtValidationResultType.TOKEN_PROCESS_ERROR);
        }
        return validationResult;
    }

    @Override
    public User getCurrentUser(String token) {
        JwtValidationResult validationResult = validate(token);
        if (validationResult != null && validationResult.getResultType() == JwtValidationResultType.TOKEN_VALID) {
            return validationResult.getUser();
        }
        return null;
    }

    private String generateTokenString(String subject, int expired, Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setIssuer("TOKEN_ISSUER")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.RS256, PRIVATE_KEY);
        if (expired > 0) {
            long expMillis = nowMillis + expired * 60 * 1000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
