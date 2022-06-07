package com.example.service;

import com.example.entity.Result;
import com.example.entity.User;
import com.example.entity.param.RegisterParam;
import com.example.entity.vo.UserVO;

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
}
