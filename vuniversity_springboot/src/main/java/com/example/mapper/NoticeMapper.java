package com.example.mapper;

import com.example.entity.Notice;

import java.util.List;

public interface NoticeMapper {

    /**
     * 查询用户点赞/评论/关注下最新的通知
     *
     * @param userId 用户id
     * @param type   类型 0点赞，1评论，2关注
     * @return
     */
    Notice getLatestNotice(int userId, int type);

    /**
     * 查询用户点赞/评论/关注下通知数量
     *
     * @param userId 用户id
     * @param type   类型 0点赞，1评论，2关注
     * @return
     */
    int getNoticeCount(int userId, int type);

    /**
     * 查询未读通知的消息数量（如果不传入type，则查询所有通知的未读数量）
     *
     * @param userId
     * @param type
     * @return
     */
    int getNoticeUnreadCount(int userId, int type);

    /**
     * 查询用户点赞/评论/关注下的通知列表
     *
     * @param userId
     * @param type
     * @return
     */
    List<Notice> getAllNotices(int userId, int type);

    /**
     * 修改通知的状态
     *
     * @param ids   需要修改的消息id集合，当用户打开会话后，需要将所有未读通知设置为已读
     * @param state 消息的状态 0未读/1已读/2删除
     * @return
     */
    int updateNoticeState(List<Long> ids, int state);

    /**
     * 增加一条通知
     *
     * @param notice
     * @return
     */
    int insertNotice(Notice notice);
}
