package ltd.fdsa.cloud.controller;

import ltd.fdsa.cloud.util.JwtUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname LoginController
 * @Description TODO
 * @Date 2019/12/20 13:40
 * @Author 高进
 */
@RestController
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login() {
        String userId = "1";
        String userName = "gaojin";
        String roles = "admin,user";
        return JwtUtil.generateJWT(userId, userName, roles);
    }
}
