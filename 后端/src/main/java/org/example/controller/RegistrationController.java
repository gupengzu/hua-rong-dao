package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.pojo.RegistrationRequest;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/registry")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @GetMapping
    public Result sendVerificationCode(@RequestParam String email, HttpServletRequest request){
        //先判断邮箱是否有效
        if(email==null||email.isEmpty()){
            return Result.error("请提供有效的邮箱地址");
        }
        //生成随机的验证码
        Random random=new Random();
        int randomNumber=100000+random.nextInt(900000);
        String code=String.valueOf(randomNumber);

        //获取当前请求的HttpSession对象，将生成的验证码和验证码的过期时间存储在会话中
        HttpSession session=request.getSession();

        try {
            registrationService.sendEmail(email,code);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("发送验证码失败，请稍后重试");
        }
    }

    @PostMapping
    public Result register(@RequestBody RegistrationRequest requestBody, HttpServletRequest request) {
        String email = requestBody.getEmail();
        String username = requestBody.getUsername();
        String password = requestBody.getPassword();
        String confirmPassword = requestBody.getConfirmPassword();
        String verificationCode = requestBody.getVerificationCode();

        HttpSession session=request.getSession();
        String storedCode=(String)session.getAttribute("verificationCode");
        Long codeExpirationTime=(Long)session.getAttribute("codeExpirationTime");

        if (codeExpirationTime == null) {
            return Result.error("验证码过期时间未设置，请重新获取验证码");
        }

        if(System.currentTimeMillis()>codeExpirationTime){
            return Result.error( "验证码已过期，请重新获取。");
        }
        if(!verificationCode.equals(storedCode)){
            return Result.error("验证码不正确，请重新输入。");
        }
        if(!password.equals(confirmPassword)){
            return Result.error("两次输入的密码不一致，请重新输入。");
        }

        //向数据库中添加user
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setImage("https://java-ai-gu.oss-cn-beijing.aliyuncs.com/2025/04/1c8a47f9-ffcc-4598-a271-465f8758a6e8.jpg");
        user.setEmail(email);
        user.setAward_count(0);

        registrationService.add(user);

        return Result.success();
    }

}
