package com.example.controller;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.param.PasswordParam;
import com.example.entity.param.RegisterParam;
import com.example.entity.vo.KaptchaCodeVO;
import com.example.entity.vo.UserVO;
import com.example.handle.GlobalException;
import com.example.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Value("${vuniversity.avatar-upload-path}")
    private String avatarUploadPath;


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

    @ApiOperation("更改密码")
    @PutMapping("/updatePassword")
    @PreAuthorize("isAuthenticated()")  // 只有登录才能修改密码
    public Result updatePassword(@RequestBody PasswordParam passwordParam) {
        // 获取用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 为了安全起见，用户修改密码时需要再一次输入密码，服务器应判断此密码是否正确
        if (!new BCryptPasswordEncoder().matches(passwordParam.getOldPassword(), user.getPassword())) {
            return Result.error(401, "密码错误！");
        }
        int flag = userService.updatePassword(user.getId(), passwordParam.getNewPassword());
        return flag == 0 ? Result.error() : Result.ok();
    }

    @ApiOperation("更改头像")
    @PutMapping("/updateAvatar")
    @PreAuthorize("isAuthenticated()")  // 只有登录才能修改头像
    public Result updateAvatar(@RequestParam MultipartFile headerImage) {

        // 1.头像存储文件夹
        File folderPath = new File(avatarUploadPath);  // 目标存储的文件夹
        if (!folderPath.exists()) {      //判断文件夹是否存在,不存在则创建
            folderPath.mkdirs();
        }
        // 2.头像名称
        String fileName = headerImage.getOriginalFilename();  // 原始文件名
        String suffix = fileName.substring(fileName.lastIndexOf(".")); // 原始文件名后缀
        if (suffix == null || "".equals(suffix)) {
            return Result.error(400, "文件格式不正确！");
        }
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix; // 新文件名 = UUID + 后缀
        // 3. 绝对路径 = 头像存储文件夹 + 头像名称
        File destFile = new File(folderPath, newFileName);
        // 4. 关键步骤，将传进来的源文件保存到目标存储文件中
        try {
            headerImage.transferTo(destFile);
        } catch (IOException e) {
            throw new GlobalException("上传文件失败，服务器发生异常");
        }
        // 5. 更新用户头像路径
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String avatarUrl = "/user/avatar/" + newFileName;
        userService.updateAvatar(user.getId(), avatarUrl);
        return Result.ok(avatarUrl);
    }

    @ApiOperation("更改用户状态")
    @PutMapping("/updateState")
    @PreAuthorize("hasRole('ADMIN')")  // 只有管理员才能修改用户状态（正常/禁用）
    public Result updateState(@RequestParam int userId, @RequestParam int state) {
        userService.updateState(userId, state);
        return Result.ok();
    }

    @ApiOperation("获取用户主页信息")
    @GetMapping("/{userId}")
    public Result getUserInfo(@PathVariable("userId") int userId) {
        // TODO 以后实现点赞/关注等功能后，需要重新建立用于显示个人主页信息的vo类，目前简单的返回User类
        UserVO userVO = userService.getUserVOById(userId);
        if (userVO.getId() == 0) {
            return Result.error(400, "用户不存在！");
        }
        return Result.ok(userVO);
    }

    @ApiOperation("获取登录验证码")
    @GetMapping("/kaptchaCode")
    public Result getKaptchaCode() {
        KaptchaCodeVO result = userService.getKaptchaCode();
        return result == null ? Result.error(400, "获取验证码失败！") : Result.ok(result);
    }
}
