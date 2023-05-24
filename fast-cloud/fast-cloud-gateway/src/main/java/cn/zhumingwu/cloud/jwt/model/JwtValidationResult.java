package cn.zhumingwu.cloud.jwt.model;

import lombok.Data;


@Data
public class JwtValidationResult {
    private JwtValidationResultType resultType;
    private User user;
}
