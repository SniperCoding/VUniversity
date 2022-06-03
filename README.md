# 微学堂

**项目简介**：微学堂是一个前后端分离的多用户论坛项目，实现了用户注册、登录、发帖、评论、私信、点赞、关注、搜索、记录日志、敏感词过滤等功能。

**技术栈**：Spring Boot + MyBatis + Redis + Spring Security + RabbitMQ + Elasticsearch 

**主要设计**：

- 使用 Spring Security 进行权限控制，并结合 JWT 实现系统的认证和授权功能；
- 使用 RabbitMQ 处理用户点赞、评论、关注行为后的系统通知，实现了模块之间的解耦与异步调用；
- 使用 Redis 实现帖子点赞、用户关注、验证码校验等功能；
- 使用 Elasticsearch 做全局搜索，并实现关键词高亮显示等功能；
- 使用 Spring 的 AOP 实现统一日志记录功能。
