package com.example.service;

import com.example.entity.User;
import com.example.entity.vo.UserVO;

public interface UserService {
    /**
     * 根据id查询用户信息
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
}
