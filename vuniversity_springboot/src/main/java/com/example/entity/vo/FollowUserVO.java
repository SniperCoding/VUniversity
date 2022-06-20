package com.example.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserVO {
    // 用户Id
    private Integer id;
    // 用户名
    private String username;
    // 头像
    private String avatar;
    // 关注时间
    private Date followTime;
}
