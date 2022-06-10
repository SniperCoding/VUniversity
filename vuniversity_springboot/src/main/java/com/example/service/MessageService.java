package com.example.service;


import com.example.entity.param.MessageParam;
import com.example.entity.param.PageParam;

import java.util.Map;

public interface MessageService {
    /**
     * 分页查询私信会话列表
     *
     * @param pageParam 分页信息
     * @return
     */
    Map<String, Object> getConversations(PageParam pageParam);

    /**
     * 获取用户所有未读消息的数量
     * @param userId
     * @return
     */
    int getAllUnreadMessageCount(int userId);

    /**
     * 根据会话id获取到会话中的所有消息
     *
     * @param param 包含分页信息
     * @param conversationId 会话id
     * @return
     */
    Map<String, Object> getAllMessagesByConversationId(PageParam param,String conversationId);

    /**
     * 增加一条消息
     * @param messageParam 包含发信人id、收信人id和私信内容
     * @return
     */
    void saveMessage(MessageParam messageParam) throws Exception;
}
