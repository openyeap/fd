package cn.zhumingwu.base.token;

import java.util.Map;

public interface SimpleWebTokenGenerator {
    /**
     * @param userId 用户Id
     * @param clientId 客户Id
     * @param claims 用户申明
     * @param roles 用户角色
     * @return 简单网络口令
     */
      SimpleWebToken generate(String userId, String clientId, Map<String, String> claims, String... roles);

    /**
     * @param refreshToken 刷新Token
     * @return 简单网络口令
     */
    SimpleWebToken refresh(String refreshToken);
}
