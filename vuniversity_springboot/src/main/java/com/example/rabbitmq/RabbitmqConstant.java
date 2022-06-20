package com.example.rabbitmq;

public class RabbitmqConstant {

    // 系统通知相关
    public static final String NOTICE_QUEUE = "notice.queue";
    public static final String NOTICE_EXCHANGE = "notice.exchange";
    public static final String NOTICE_KEY = "notice";

    // 新增或修改帖子相关
    public static final String POST_INSERT_QUEUE = "post.insert.queue";
    public static final String POST_INSERT_EXCHANGE = "post.insert.exchange";
    public static final String POST_INSERT_KEY = "post.insert.notice";

    // 删除帖子相关
    public static final String POST_DELETE_QUEUE = "post.delete.queue";
    public static final String POST_DELETE_EXCHANGE = "post.delete.exchange";
    public static final String POST_DELETE_KEY = "post.delete.notice";
}
