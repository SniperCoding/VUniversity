package com.example.entity.vo;

import com.example.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    // 主键
    private Integer id;
    // 用户名
    private String username;
    // 头像
    private String avatar;
    // 邮箱
    private String email;
    // 注册时间
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date registerTime;
    // 角色列表
    private List<Role> roles;
    // 点赞数
    private Integer likeCount;
    // 关注数
    private Integer FolloweeCount;
    // 粉丝数
    private Integer FollowerCount;
}
