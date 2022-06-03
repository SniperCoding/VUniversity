package com.example.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserVO{
	// 主键
    private Integer id;
    // 帖子用户id
    private Integer userId;
    // 标题
    private String title;
    // 内容
    private String content;
    // 帖子点赞量
    private Integer likeCount;
    // 当前用户是否对帖子进行了点赞[true表示已点赞，false表示未点赞]
    private Boolean likeState;
    // 帖子评论数量
    private Integer commentCount;
    // 帖子分数，用于计算帖子热度
    private Double score;
    // 帖子类型：0正常 1加精
    private Integer type;
    // 帖子状态：0正常 1置顶 2删除
    private Integer state;
    // 帖子创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    // 帖子修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    // 帖子作者用户名
    private String username;
    // 帖子作者头像
    private String avatar;
}
