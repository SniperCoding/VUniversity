package com.example.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {
    // 评论id
    private Long id;
    // 评论内容
    private String content;
    // 评论者id
    private Integer userId;
    // 评论者用户名
    private String username;
    // 评论者头像
    private String avatar;
    // 被评论者id,，如果是0，就表示对当前帖子（针对一级评论）或者当前层的层主（针对二级评论）进行回复
    private Integer toUserId;
    // 被评论者用户名，如果是null，就表示对当前帖子（针对一级评论）或者当前层的层主（针对二级评论）进行回复
    private String toUsername;
    // 评论等级
    private Integer level;
    // 评论点赞量
    private Integer likeCount;
    // 当前用户是否对评论进行了点赞
    private Boolean likeState;
    // 评论时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    // 子评论
    private List<CommentVO> children;
}
