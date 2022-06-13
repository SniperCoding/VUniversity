package com.example.service.impl;

import com.example.entity.Comment;
import com.example.entity.User;
import com.example.entity.param.CommentPageParam;
import com.example.entity.param.CommentParam;
import com.example.entity.vo.CommentVO;
import com.example.entity.vo.PostUserVO;
import com.example.mapper.CommentMapper;
import com.example.service.CommentService;
import com.example.service.LikeService;
import com.example.service.PostService;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.example.util.SensitiveFilterUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitiveFilterUtil sensitiveFilterUtil;
    @Autowired
    private PostService postService;
    @Autowired
    private LikeService likeService;

    @Override
    public List<CommentVO> getAllComments(CommentPageParam commentPageParam) {
        // 1.分页条件、评论等级和实体id
        int pageNum = commentPageParam.getPageNum();
        int pageSize = commentPageParam.getPageSize();
        int commentLevel = commentPageParam.getLevel();
        Long parentId = commentPageParam.getParentId();
        // 一页最多展示 20 条
        if (pageSize > 20) {
            pageSize = 20;
        }
        // 2.执行分页
        PageHelper.startPage(pageNum, pageSize);
        // 3.查询数据库，获取所有评论
        List<Comment> comments = commentMapper.getAllComments(commentLevel,parentId);
        // 4.封装查询结果
        List<CommentVO> commentVOs = new ArrayList<>();
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 遍历所有评论，并获取评论的用户信息
        if (comments != null) {
            for (Comment comment : comments) {
                CommentVO commentVO = new CommentVO();
                // 4.1 复制属性
                BeanUtils.copyProperties(comment, commentVO);
                // 4.2 获取评论者信息
                User user = userService.getUserById(comment.getFromUserId());
                commentVO.setAvatar(user.getAvatar());
                commentVO.setUsername(user.getUsername());
                commentVO.setUserId(user.getId());
                // 4.3 获取被评论者信息,如果为0，说明是对层主做的评论，则不处理，否则是对其他人做的评论
                if (comment.getToUserId() != 0) {
                    User toUser = userService.getUserById(comment.getToUserId());
                    commentVO.setToUsername(toUser.getUsername());
                }
                // 4.4 获取评论点赞量
                long commentLikeCount = likeService.getPostOrCommentLikeCount(1, comment.getId());
                commentVO.setLikeCount((int) commentLikeCount);
                // 4.5 查看当前用户是否对用户进行了点赞
                if(currentUser instanceof User){
                    boolean like = likeService.isLike(((User)currentUser).getId(), 1, comment.getId());
                    commentVO.setLikeState(like);
                }
                // 4.6 如果当前是一级评论，则需要查询其子评论，即二级评论
                if(commentLevel==ConstantUtil.COMMENT_LEVEL_1){
                    // 默认查第一页
                    CommentPageParam pageParam = new CommentPageParam(0,pageSize,ConstantUtil.COMMENT_LEVEL_2,comment.getId());
                    commentVO.setChildren( getAllComments(pageParam));
                }
                commentVOs.add(commentVO);
            }
        }
        return commentVOs;
    }

    @Override
    @Transactional
    public void saveComment(CommentParam commentParam) {
        Comment comment = new Comment();
        // 复制属性
        BeanUtils.copyProperties(commentParam,comment);
        // 获取用户id
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setFromUserId(user.getId());
        // 评论内容敏感词过滤和字符转移
        String content = HtmlUtils.htmlEscape(sensitiveFilterUtil.filterKeyword(commentParam.getContent()));
        comment.setContent(content);
        // 状态
        comment.setState(0);
        // 创建时间
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);

        // 如果是一级评论，则将帖子评论数量加1
        if(commentParam.getLevel()==ConstantUtil.COMMENT_LEVEL_1){
            long parentId = comment.getParentId();
            PostUserVO post = postService.getPostById((int) parentId);
            postService.updateCommentCount((int) parentId,post.getCommentCount()+1);
        }
    }
}
