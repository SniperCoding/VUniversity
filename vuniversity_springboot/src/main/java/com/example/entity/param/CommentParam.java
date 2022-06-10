package com.example.entity.param;

import lombok.Data;

@Data
public class CommentParam {
    // 发送给谁
    private Integer toUserId;
    // 发送内容
    private String content;
    // 评论等级
    private Integer level;
    // 如果是一级评论，则传入帖子id，如果是二级评论，则传入楼层评论id
    private Long parentId;
}
