package com.example.mapper;

import com.example.entity.Role;
import com.example.entity.User;

import java.util.List;

public interface UserMapper {
    /**
     * 根据id查询用户信息
     *
     * @param UserId
     * @return
     */
    User getUserById(int UserId);

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
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 根据用户id，获取用户角色列表
     *
     * @param userId
     * @return
     */
    List<Role> getRolesByUserId(int userId);

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
}
