package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.LoginInfo;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        log.info("用户登录:{}",user);
        LoginInfo loginInfo=userService.login(user);
        if(loginInfo!=null){
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误");
    }
}
