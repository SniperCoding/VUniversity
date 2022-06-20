package com.example.service.impl;

import com.example.entity.Post;
import com.example.entity.param.SearchParam;
import com.example.service.ElasticSearchService;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchServiceImpl implements ElasticSearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public Map<String, Object> search(SearchParam params) {
        // 1.构建搜索条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                // 按关键字查询
                .withQuery(QueryBuilders.multiMatchQuery(params.getKey(), "content", "title"))
                // 分页
                .withPageable(PageRequest.of(params.getPageNum(), params.getPageSize()))
                // 高亮
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        // 2.进行搜索
        SearchHits<Post> searchHits = elasticsearchRestTemplate.search(searchQuery, Post.class);
        // 3.封装搜索结果
        // 3.1.获取总条数
        long totalHits = searchHits.getTotalHits();
        // 3.2 获取所有搜索记录
        List<Post> searchPosts = new ArrayList<>();
        for (SearchHit<Post> hit : searchHits) {
            // 获取搜索的帖子内容（注意：这里得到的并没有高亮结果，es的高亮查询不会直接把高亮字段映射到实体类，）
            Post content = hit.getContent();
            Post post = new Post();
            // 复制属性
            BeanUtils.copyProperties(content, post);
            // 添加高亮后的标题
            List<String> list1 = hit.getHighlightFields().get("title");
            if (list1 != null) {
                post.setTitle(list1.get(0));
            }
            // 添加高亮后的内容
            List<String> list2 = hit.getHighlightFields().get("content");
            if (list2 != null) {
                post.setContent(list2.get(0));
            }
            // 添加到集合中
            searchPosts.add(post);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", totalHits);
        result.put("searchPosts", searchPosts);
        return result;
    }

    @Override
    public void savePost(Post post) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(post.getId().toString()) // 指定文档id
                .withObject(post)   // 指定文档
                .build();
        elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("post")); // 索引名
    }

    @Override
    public void deletePost(Integer id) {
        elasticsearchRestTemplate.delete(id.toString(), Post.class);
    }
}
