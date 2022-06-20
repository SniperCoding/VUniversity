package com.example.service;

import com.example.entity.Post;
import com.example.entity.param.SearchParam;

import java.util.Map;

public interface ElasticSearchService {

    /**
     * @param params 搜索条件，包括搜素关键字、页码、每页数量和排序字段
     * @return
     */
    Map<String, Object> search(SearchParam params);


    /**
     * 插入或修改帖子
     *
     * @param post
     */
    void savePost(Post post);
}
