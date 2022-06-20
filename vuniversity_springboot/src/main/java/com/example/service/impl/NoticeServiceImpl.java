package com.example.service.impl;

import com.example.entity.Notice;
import com.example.entity.User;
import com.example.entity.param.NoticePageParam;
import com.example.mapper.NoticeMapper;
import com.example.service.NoticeService;
import com.example.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Notice getLatestNotice(int userId, int type) {
        Notice latestNotice = noticeMapper.getLatestNotice(userId, type);
        // 获取点赞/评论/关注当前用户的用户信息（不存数据库，数据库只存了id）
        latestNotice.setFromUser(userService.getUserVOById(latestNotice.getFromUserId()));
        return latestNotice;
    }

    @Override
    public int getNoticeCount(int userId, int type) {
        return noticeMapper.getNoticeCount(userId, type);
    }

    @Override
    public int getNoticeUnreadCount(int userId, int type) {
        return noticeMapper.getNoticeUnreadCount(userId, type);
    }

    @Override
    public Map<String, Object> getAllNotices(NoticePageParam param) {
        // 1.分页条件、用户id
        int pageNum = param.getPageNum();
        int pageSize = param.getPageSize();
        int userId = param.getUserId();
        int type = param.getType();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != userId) {
            return null;
        }
        if (pageSize > 20) {
            pageSize = 20;
        }
        // 2.执行分页
        PageHelper.startPage(pageNum, pageSize);
        // 3.查询数据库并封装结果
        Map<String, Object> result = new HashMap<>();
        List<Notice> allNotices = noticeMapper.getAllNotices(userId, type);
        for (Notice notice : allNotices) {
            notice.setFromUser(userService.getUserVOById(notice.getFromUserId()));
        }
        result.put("allNotices", allNotices);
        result.put("noticeUnreadCount", getNoticeUnreadCount(userId, type));
        return result;
    }

    @Override
    public int updateNoticeState(List<Long> ids, int state) {
        return noticeMapper.updateNoticeState(ids, state);
    }

    @Override
    public int saveNotice(Notice notice) {
        return noticeMapper.insertNotice(notice);
    }
}
