package com.example;

import com.example.entity.Post;
import com.example.mapper.PostMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.util.List;

@SpringBootTest
public class ElasticSearchTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private PostMapper postMapper;

    // 创建索引
    @Test
    public void CreateIndex() {
        // 获取实体类
        Class clazz = Post.class;
        // 创建索引操作对象
        IndexOperations indexOps = elasticsearchRestTemplate.indexOps(clazz);
        indexOps.create(); //创建索引
        indexOps.putMapping(indexOps.createMapping(clazz)); //设置索引的映射规则
    }

    // 增加文档
    @Test
    public void addDocument() {
        List<Post> posts = postMapper.findAll(0, 0);
        for (Post post : posts) {
            // 将帖子插入到es中
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(post.getId().toString()) // 指定文档id
                    .withObject(post)   // 指定文档
                    .build();
            elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("post")); // 索引名
        }
    }
}
