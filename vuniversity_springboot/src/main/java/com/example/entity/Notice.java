package com.example.entity;

import com.example.entity.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    // 主键
    private Long id;
    // 通知类型 0点赞/1评论/2关注
    private Integer type;
    // 被通知者id
    private Integer userId;
    // 点赞/评论/关注此用户的用户id
    private Integer fromUserId;
    // 点赞/评论/关注此用户的用户信息（不存数据库）
    private UserVO fromUser;
    // 状态：0未读/1已读/2删除
    private Integer state;
    // 通知时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
