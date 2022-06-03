package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    // 主键
    private Long id;
    // 评论者id
    private Integer fromUserId;
    // 被评论者id，如果是0，就表示对当前帖子（针对一级评论）或者当前层的层主（针对二级评论）进行回复
    private Integer toUserId;
    // 内容
    private String content;
    // 评论类型： 1回复帖子（即一级评论） 2回复评论（即二级评论）
    private Integer level;
    // 评论的父层id,即当前帖子id/当前层主评论id
    private Long parentId;
    // 状态：0正常/1删除
    private Integer state;
    // 评论时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
