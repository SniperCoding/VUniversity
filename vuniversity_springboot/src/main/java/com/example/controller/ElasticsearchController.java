package com.example.controller;

import com.example.entity.Result;
import com.example.entity.param.SearchParam;
import com.example.service.ElasticSearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class ElasticsearchController {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @ApiOperation("插入帖子")
    @PostMapping("/posts")
    public Result searchPosts(@RequestBody SearchParam searchParam){
        return Result.ok(elasticSearchService.search(searchParam));
    }
}
