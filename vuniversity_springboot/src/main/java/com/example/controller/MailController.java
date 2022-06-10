package com.example.controller;

import com.example.entity.Result;
import com.example.service.MailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @ApiOperation("注册验证码")
    @GetMapping("/registerCode")
    public Result getRegisterCode(@RequestParam() String mail) {
        boolean isMailExist;
        try {
            isMailExist = mailService.sendMail(mail);  // 抛出异常说明用户邮箱不可用
        } catch (Exception e) {
            return Result.error(400, "邮箱不正确或不可用！");
        }
        // 如果邮箱不合法已被注册
        return isMailExist ? Result.error(400, "邮箱格式不合法或已被注册！") : Result.ok();
    }
}
