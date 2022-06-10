package com.example.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationVO {
    // 消息主键id
    private Long id;
    // 收信人id
    private Integer toUserId;
    // 收信人名称
    private String toUsername;
    // 收信人头像
    private String toUserAvatar;
    // 会话唯一标识
    private String conversationId;
    // 最新一条私信内容
    private String content;
    // 会话中所有消息数量
    private Integer allMessageCount;
    // 未读消息数量
    private Integer unreadMessageCount;
    // 发送时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
