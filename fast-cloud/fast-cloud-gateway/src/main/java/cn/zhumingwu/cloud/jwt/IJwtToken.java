package cn.zhumingwu.cloud.jwt;


import cn.zhumingwu.cloud.jwt.model.JwtResult;
import cn.zhumingwu.cloud.jwt.model.JwtValidationResult;
import cn.zhumingwu.cloud.jwt.model.User;


public interface IJwtToken {
    JwtResult generate(User user);

    JwtResult refreshGenerate(String oldRefreshToken);

    JwtValidationResult validate(String token);

    User getCurrentUser(String token);
}
