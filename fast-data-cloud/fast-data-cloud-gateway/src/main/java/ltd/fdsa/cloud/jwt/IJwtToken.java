package ltd.fdsa.cloud.jwt;


import ltd.fdsa.cloud.jwt.model.JwtResult;
import ltd.fdsa.cloud.jwt.model.JwtValidationResult;
import ltd.fdsa.cloud.jwt.model.User;


public interface IJwtToken {
    JwtResult generate(User user);

    JwtResult refreshGenerate(String oldRefreshToken);

    JwtValidationResult validate(String token);

    User getCurrentUser(String token);
}
