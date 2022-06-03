package com.example.mapper;

import com.example.entity.User;

public interface UserMapper {
    /**
     * 根据id查询用户信息
     *
     * @param UserId
     * @return
     */
    User getUserById(int UserId);
}
