package com.example.service;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.param.RegisterParam;
import com.example.entity.vo.KaptchaCodeVO;
import com.example.entity.vo.UserVO;

import java.util.Map;

public interface UserService {
    /**
     * 根据id查询用户信息
     *
     * @param UserId
     * @return
     */
    User getUserById(int UserId);

    /**
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    UserVO getUserVOById(int userId);

    /**
     * 根据用户名查询用户信息
     *
     * @param userName
     * @return
     */
    User getUserByName(String userName);

    /**
     * 插入用户
     *
     * @param registerParam
     * @return 直接返回响应json结果
     */
    Result saveUser(RegisterParam registerParam);

    /**
     * 根据refreshToken获取新token
     *
     * @param token
     * @return
     */
    Result getNewTokenByRefreshToken(String token);

    /**
     * 根据id修改密码
     *
     * @param userId
     * @param newPassword
     * @return
     */
    int updatePassword(int userId, String newPassword);

    /**
     * 根据id修改头像
     *
     * @param userId
     * @param newAvatar
     * @return
     */
    int updateAvatar(int userId, String newAvatar);

    /**
     * 根据id修改用户状态
     *
     * @param userId
     * @param state
     * @return
     */
    int updateState(int userId, int state);


    /**
     * 获取登录验证码
     * @return 存放有图片的base64编码以及验证码的key
     */
    KaptchaCodeVO getKaptchaCode();
}
