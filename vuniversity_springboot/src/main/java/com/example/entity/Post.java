package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "post")        // 实体类与 ES 的对应关系
public class Post {
    // 主键
    @Id
    private Integer id;

    // 帖子用户id
    @Field(type = FieldType.Integer) // 属性与es对应关系
    private Integer userId;

    // 标题
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")   //【重点,分词】
    private String title;

    // 内容
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")  //【重点，分词】
    private String content;

    // 帖子评论数量
    @Field(type = FieldType.Integer)
    private Integer commentCount;

    // 帖子分数，用于计算帖子热度
    @Field(type = FieldType.Double)
    private Double score;

    // 帖子类型：0正常 1加精
    @Field(type = FieldType.Integer)
    private Integer type;

    // 帖子状态：0正常 1置顶 2删除
    @Field(type = FieldType.Integer)
    private Integer status;

    // 帖子创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field(type = FieldType.Date)
    private Date createTime;

    // 帖子修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field(type = FieldType.Date)
    private Date updateTime;
}
