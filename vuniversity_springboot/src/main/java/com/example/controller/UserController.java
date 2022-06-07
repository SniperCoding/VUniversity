package com.example.controller;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.param.RegisterParam;
import com.example.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("用户名是否存在")
    @GetMapping("/exist")
    public Result isUserExist(@RequestParam String userName) {
        User user = userService.getUserByName(userName);
        if (user != null) {
            return Result.error(400, "用户名已存在！");
        }
        return Result.ok();
    }

    @ApiOperation("注册用户")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterParam registerParam) {
        return userService.saveUser(registerParam);
    }

    @ApiOperation("获取新token")
    @PostMapping("/getNewToken")
    @PreAuthorize("isAuthenticated()")
    public Result getNewTokenByRefreshToken(@RequestBody String token) {
        return userService.getNewTokenByRefreshToken(token);
    }
}
