package com.example.rabbitmq;

import com.example.entity.Notice;
import com.example.entity.Post;
import com.example.service.ElasticSearchService;
import com.example.service.NoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.rabbitmq.RabbitmqConstant.*;

@Component
public class RabbitmqListener {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private ElasticSearchService elasticSearchService;

    /**
     * 监听点赞/评论/关注通知事件
     *
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = NOTICE_QUEUE),          // 队列名称
            exchange = @Exchange(name = NOTICE_EXCHANGE), // 交换机名称（默认为direct定向类型）
            key = {NOTICE_KEY})                           // key 可以写多个，用逗号分隔开
    )
    public void noticeQueueListener(String message) throws JsonProcessingException {
        // 1.获取消息，并将其转换为通知类型
        Notice notice = new ObjectMapper().readValue(message, Notice.class);
        // 2.将通知存入到数据库中
        noticeService.saveNotice(notice);
    }

    /**
     * 监听插入/修改帖子事件
     *
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = POST_INSERT_QUEUE),          // 队列名称
            exchange = @Exchange(name = POST_INSERT_EXCHANGE), // 交换机名称（默认为direct定向类型）
            key = {POST_INSERT_KEY})                           // key 可以写多个，用逗号分隔开
    )
    public void postInsertOrUpdateQueueListener(String message) throws JsonProcessingException {
        // 1.获取消息，并将其转换为帖子类型
        Post post = new ObjectMapper().readValue(message, Post.class);
        // 2.将通知存入到ES中
        elasticSearchService.savePost(post);
    }

    /**
     * 监听删除帖子事件
     *
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = POST_DELETE_QUEUE),          // 队列名称
            exchange = @Exchange(name = POST_DELETE_EXCHANGE), // 交换机名称（默认为direct定向类型）
            key = {POST_DELETE_KEY})                           // key 可以写多个，用逗号分隔开
    )
    public void postDeleteQueueListener(String message) throws JsonProcessingException {
        // 1.获取消息，并将其转换为Integer类型
        Integer postId = new ObjectMapper().readValue(message, Integer.class);
        // 2.从ES中删除帖子
        elasticSearchService.deletePost(postId);
    }
}
