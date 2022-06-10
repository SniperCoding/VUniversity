package com.example.mapper;

import com.example.entity.Comment;

import java.util.List;

public interface CommentMapper {

    /**
     * 查询全部评论
     *
     * @param level    评论等级，1表示对帖子进行回复，即一级评论，2表示对评论进行回复，即二级评论
     * @param parentId 当前层主的评论id（针对二级评论），0（针对一级评论）
     * @return
     */
    List<Comment> getAllComments(int level, Long parentId);

    /**
     * 插入一条评论
     * @param comment
     * @return
     */
    int insertComment(Comment comment);
}
