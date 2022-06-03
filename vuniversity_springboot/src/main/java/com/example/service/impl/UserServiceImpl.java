package com.example.service.impl;

import com.example.entity.User;
import com.example.entity.vo.UserVO;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

   	@Override
    public UserVO getUserVOById(int userId) {
        User user = getUserById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }
}
