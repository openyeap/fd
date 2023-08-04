package cn.zhumingwu.client.controller;

import cn.zhumingwu.base.model.HttpCode;
import cn.zhumingwu.base.model.Result;
import cn.zhumingwu.client.entity.User;
import cn.zhumingwu.client.service.UserService;

import cn.zhumingwu.client.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import cn.zhumingwu.client.view.Token;
import cn.zhumingwu.client.view.UserLogin;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController extends BaseController {
    @Autowired
    private UserServiceImpl service;

    @GetMapping(value = "/update")
    public Result<Object> update(@RequestBody User entity) {
        try {
            return Result.success(this.service.update(entity));
        } catch (Exception ex) {
            return Result.error(ex);
        }
    }


    //    @RequestMapping(value = {"login"}, method = {RequestMethod.POST})
    @ResponseBody
    @PostMapping("/login")
    public Result<Token> login(UserLogin login) {
        var result = this.service.login(login.getUsername(), login.getPassword());
        if (result.isOk()) {
            var swt = result.getData();
            var token = Token.builder()
                    .userId(swt.getUserId())
                    .corpId(swt.getCorpId())
                    .expiredIn(swt.getExpiredIn())
                    .createTime(LocalDateTime.now())
                    .refreshToken(swt.getRefreshToken()).build();
            return Result.success(token);
        }
        return Result.fail(HttpCode.BAD_REQUEST);
    }
}
