package com.example.mapper;

import com.example.entity.Message;

import java.util.List;

public interface MessageMapper {
    /**
     * 查询用户的全部会话列表，并且每个会话只返回一条最新的私信.
     * @param userId 用户id
     * @return
     */
    List<Message> getConversations(int userId);

    /**
     * 根据会话id获取到此会话中的所有消息数量
     * @param conversationId
     * @return
     */
    Integer getAllMessageCount(String conversationId);

    /**
     * 根据会话id获取到此会话中的所有未读消息数量（未读即当前用户是收信人，且消息的状态是未读，也就是state=0）
     * 如果不传入id，则查询用户的所有未读消息
     * @param toUserId 收信人
     * @param conversationId 会话id
     * @return
     */
    Integer getUnreadMessageCount(int toUserId, String conversationId);

    /**
     * 根据会话id获取到会话中的所有消息
     * @param conversationId
     * @return
     */
    List<Message> getAllMessagesByConversationId(String conversationId);

    /**
     * 修改消息的状态
     * @param ids 需要修改的消息id集合，比如当用户打开会话后，需要将所有未读消息设置为已读
     * @param state 消息的状态 0未读/1已读/2删除
     * @return
     */
    int updateMessageState(List<Long> ids,int state);

    /**
     * 增加一条消息
     * @param message
     * @return
     */
    int insertMessage(Message message);
}
