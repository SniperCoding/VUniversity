package com.example.controller;

import com.example.entity.Result;
import com.example.entity.param.CommentPageParam;
import com.example.entity.param.CommentParam;
import com.example.entity.vo.CommentVO;
import com.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/getAll")
    public Result getAll(@RequestBody CommentPageParam pageParam) {
        List<CommentVO> allComments = commentService.getAllComments(pageParam);
        return Result.ok(allComments);
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public Result saveComment(@RequestBody CommentParam commentParam) {
        commentService.saveComment(commentParam);
        return Result.ok();
    }
}
