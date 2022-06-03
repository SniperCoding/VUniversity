package com.example.service.impl;

import com.example.mapper.MailMapper;
import com.example.service.MailService;
import com.example.util.MailUtil;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private MailMapper mailMapper;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean isMailExist(String mail) {
        return mailMapper.existMail(mail) == 1;
    }

    @Override
    public boolean sendMail(String to) throws Exception {
        // 如果邮箱不合法或已被注册，返回true
        if (!mailUtil.isEmail(to)||isMailExist(to)) {
            return true;
        }
        // 发送邮件，并获取发送的验证码
        String registerCode = mailUtil.sendHtmlMail(to);
        // 将验证码存放到redis中,过期时间五分钟
        String registerCodeKey = RedisKeyUtil.getRegisterCode(to);
        redisTemplate.opsForValue().set(registerCodeKey, registerCode,5*60, TimeUnit.SECONDS);
        return false;
    }
}
