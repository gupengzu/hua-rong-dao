package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.mapper.UserMapper;
import org.example.pojo.User;
import org.example.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserMapper userMapper;
    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendEmail(String email, String code) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(email);//email是目标用户的邮箱
        message.setSubject("华容道注册验证码");
        message.setText("你的注册验证码是：" + code + "，有效期为 5 分钟。");
        javaMailSender.send(message);
    }

    @Override
    public void add(User user) {
        userMapper.add(user.getUsername(),user.getPassword(),user.getEmail(),user.getAward_count(),user.getImage());
    }
}
