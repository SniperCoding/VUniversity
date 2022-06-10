package com.example.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPageParam {
    // 页码
    private Integer pageNum;

    // 每页数量
    private Integer pageSize;

    // 评论等级
    private Integer level;

    // 如果是一级评论，则传入帖子id，如果是二级评论，则传入楼层评论id
    private Long parentId;
}
