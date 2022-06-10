package com.example.service.impl;

import com.example.entity.Message;
import com.example.entity.User;
import com.example.entity.param.MessageParam;
import com.example.entity.param.PageParam;
import com.example.entity.vo.ConversationVO;
import com.example.entity.vo.UserVO;
import com.example.mapper.MessageMapper;
import com.example.service.MessageService;
import com.example.service.UserService;
import com.example.util.ConstantUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserService userService;

    @Override
    public Map<String, Object> getConversations(PageParam pageParam) {
        // 1.分页条件、用户id
        int pageNum = pageParam.getPageNum();
        int pageSize = pageParam.getPageSize();
        User fromUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId = fromUser.getId();
        if (pageSize > 20) {
            pageSize = 20;
        }
        // 2.执行分页
        PageHelper.startPage(pageNum, pageSize);
        // 3.查询数据库，获取所有私信会话列表
        List<Message> conversations = messageMapper.getConversations(userId);
        // 4.封装私信会话列表
        List<ConversationVO> conversationVOs = new ArrayList<>();
        for (Message message : conversations) {
            ConversationVO conversationVO = new ConversationVO();
            // 4.1 复制属性
            BeanUtils.copyProperties(message, conversationVO);
            // 4.2 获取会话对方信息
            int otherUserId = userId == message.getFromUserId() ? message.getToUserId() : message.getFromUserId();
            User user = userService.getUserById(otherUserId);
            conversationVO.setToUserAvatar(user.getAvatar());
            conversationVO.setToUsername(user.getUsername());
            conversationVO.setToUserId(user.getId());
            // 4.3 获取会话中的所有消息和未读消息数量
            Integer allMessageCount = messageMapper.getAllMessageCount(message.getConversationId());
            Integer unreadMessageCount = messageMapper.getUnreadMessageCount(userId, message.getConversationId());
            conversationVO.setAllMessageCount(allMessageCount);
            conversationVO.setUnreadMessageCount(unreadMessageCount);
            // 4.4 将封装后的消息存放入列表中
            conversationVOs.add(conversationVO);
        }

        // 5.获取用户全部会话中的所有未读消息总和（也可以不查数据库，在上面 for 循环中求和）
        int allUnreadMessageCount = getAllUnreadMessageCount(userId);
        // 6.进一步将会话列表和未读消息总和封装为map返回
        Map<String, Object> result = new HashMap<>();
        result.put("conversations", conversationVOs);
        result.put("allUnreadMessageCount", allUnreadMessageCount);
        return result;
    }

    @Override
    public int getAllUnreadMessageCount(int userId) {
        return messageMapper.getUnreadMessageCount(userId, null);
    }

    @Override
    @Transactional
    public Map<String, Object> getAllMessagesByConversationId(PageParam param, String conversationId) {
        // 0.分页条件
        int pageNum = param.getPageNum();
        int pageSize = param.getPageSize();
        if (pageSize > 20) {
            pageSize = 20;
        }
        // 1.判断是否恶意攻击
        User fromUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String[] minId_maxID = conversationId.split("_");
        // 如果既不是发信人也不是收信人,则直接返回null
        if(minId_maxID.length!=2 || (!fromUser.getId().toString().equals(minId_maxID[0]) && !fromUser.getId().toString().equals(minId_maxID[1]))){
            return null;
        }
        // 2.执行分页
        PageHelper.startPage(pageNum, pageSize);
        // 3.获取会话分页消息
        List<Message> messages = messageMapper.getAllMessagesByConversationId(conversationId);
        // 4.获取发信人和收信人的信息并进行封装
        String toUserId = minId_maxID[0].equals(fromUser.getId().toString()) ? minId_maxID[1] : minId_maxID[0];
        UserVO toUserVO = userService.getUserVOById(Integer.valueOf(toUserId));
        UserVO fromUserVO = new UserVO();
        BeanUtils.copyProperties(fromUser, fromUserVO);
        // 5.封装信息
        Map<String, Object> result = new HashMap<>();
        result.put("messages", messages);
        result.put("fromUser", fromUserVO);
        result.put("toUser", toUserVO);
        // 6.将未读设置为已读
        List<Long> ids = getLetterIds(fromUser.getId(), messages);
        if(!ids.isEmpty()){
            messageMapper.updateMessageState(ids, ConstantUtil.Message_READ);
        }
        return result;
    }

    // 获取未读的私信列表
    private List<Long> getLetterIds(int toUserId, List<Message> messages) {
        List<Long> ids = new ArrayList<>();
        for (Message message : messages) {
            if (toUserId == message.getToUserId() && message.getState() == 0) {
                ids.add(message.getId());
            }
        }
        return ids;
    }

    @Override
    public void saveMessage(MessageParam messageParam) throws Exception {
        User fromUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userService.getUserById(messageParam.getToUserId())==null || fromUser.getId()!=messageParam.getFromUserId()){
            throw new Exception("用户不存在！");
        }

        Message message = new Message();
        // 复制属性
        BeanUtils.copyProperties(messageParam,message);
        int fromUserId = messageParam.getFromUserId();
        int toUserId = messageParam.getToUserId();
        String conversationId = Math.min(fromUserId,toUserId)  + "_" + Math.max(fromUserId,toUserId);
        message.setConversationId(conversationId);
        // 设置默认属性
        message.setState(ConstantUtil.Message_UNREAD);
        message.setCreateTime(new Date());
        // 存储到数据库中
        messageMapper.insertMessage(message);
    }
}
