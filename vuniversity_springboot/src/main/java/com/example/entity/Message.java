package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    // 主键
    private Long id;
    // 发送者
    private Integer fromUserId;
    // 接收者
    private Integer toUserId;
    // 对话标识
    private String conversationId;
    // 内容
    private String content;
    // 状态：0未读/1已读/2删除
    private Integer state;
    // 发送时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
