package com.example.service;

import com.example.entity.param.CommentPageParam;
import com.example.entity.param.CommentParam;
import com.example.entity.vo.CommentVO;

import java.util.List;

public interface CommentService {

    /**
     * 查询全部评论
     * @param commentPageParam 包含分页页码、每页显示条数、评论等级和实体id
     * @return
     */
    List<CommentVO> getAllComments(CommentPageParam commentPageParam);

    /**
     *  插入一条评论
     * @param commentParam
     * @return
     */
    void saveComment(CommentParam commentParam);
}
