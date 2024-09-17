# IM 即时通信系统

## 时刻提醒自己
记得写注释！
记得写文档！
记得多用git！

## 待解决问题


1. 重构排行榜： 使用 cursor (新数据会影响老数据！) 
2. 敏感词问题：DFA算法(前缀树)，具体看鱼皮视频31:00

## 项目代码组织结构
吸取上次项目的教训：指同一个模块堆在一坨，导致后期代码阅读难度大

本次项目代码组织结构，决定根据模块划分开发

1. 工具模块 common

2. api模块

基本架构：controller => service => dao => mapper => domain

|    api模块    |          说明          |
|:-----------:|:--------------------:|
|    chat     |         聊天模块         |
|    user     |         用户模块         |
|    video    |         视频模块         |
|   social    |         社交模块         |
| interactive |         互动模块         |

3. websocket 模块
4. security 模块

## 技术栈

|       技术       |              说明               |                             官网                             |
|:--------------:|:-----------------------------:|:------------------------------------------------------------:|
|   SpringBoot   |           web开发必备框架           | [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| SpringSecurity |               配合 Jwt + Token 完成权限检验                |                              https://spring.io/projects/spring-security                              |
|    MyBatis     |             ORM框架             |        http://www.mybatis.org/mybatis-3/zh/index.html        |
|  MyBatisPlus   |       零sql，简化数据库操作，分页插件       |        [https://baomidou.com/](https://baomidou.com/)        |
|     Redis      |       缓存加速，多数据结构支持业务功能        |             [https://redis.io](https://redis.io)             |
|     Netty      | 异步的、事件驱动的网络应用程序框架和工具 |                              https://netty.io/                              |
|    RabbitMQ    |          消息队列，异步处理消息          |              https://www.rabbitmq.com/docs              |
|     Docker     |            应用容器引擎             |       [https://www.docker.com](https://www.docker.com)       |
|      Oss       |             对象存储              |     [https://letsencrypt.org/](https://letsencrypt.org/)     |
|      Jwt       |           用户登录，认证方案           |               [https://jwt.io](https://jwt.io)               |
|     Lombok     |             简化代码              |    [https://projectlombok.org](https://projectlombok.org)    |
|     Hutool     |           Java工具类库            |               https://github.com/looly/hutool                |
|     minio      |            自建对象存储             |                https://github.com/minio/minio                |


## 一. Netty 实现 Websocket

### 1.1 创建 Websocket 服务器

#### 1.1.1 启动和关闭
在类上，添加 @Configuration 注解，保证能够使用注解

1. start() 方法，添加 @PostConstruct 注解，启动 Netty 服务器；
2. destory() 方法，添加 @PreDestroy 注解，关闭 Netty 服务器。

#### 1.1.2 服务器配置
首先需要明白websocket的连接过程

根据websocket的升级过程，对 netty 服务器进行如下配置：
1. **IdleStateHandler** 实现心跳检测。
2. 调用 **option#(ChannelOption\<T> option, T value)** 方法，设置了服务端接受客户端的连接队列大小，开启了TCP Keepalive 机制
3. websocket初期是通过http请求，进行升级，建立双方的连接。所以编解码器需要用到 **HttpServerCodec**。
4. **HttpHeadersHandler** 是自定义的处理器。赶在websocket升级之前，**获取用户的ip地址和token信息**，然后保存到channel的附件里。
5. **WebSocketServerProtocolHandler** 是netty进行websocket升级的处理器，在这期间会抹除http相关的信息。
6. **NettyWebSocketServerHandler** 为自定义业务处理器



## 参考资料
[跟着源码学IM(七)：手把手教你用WebSocket打造Web端IM聊天-网页端IM开发/专项技术区 - 即时通讯开发者社区! (52im.net)](http://www.52im.net/thread-3483-1-1.html)

[跟着源码学IM(八)：万字长文，手把手教你用Netty打造IM聊天-IM开发/专项技术区 - 即时通讯开发者社区! (52im.net)](http://www.52im.net/thread-3489-1-1.html)

[WebSocket详解（一）：初步认识WebSocket技术-网页端IM开发/专项技术区 - 即时通讯开发者社区! (52im.net)](http://www.52im.net/thread-331-1-1.html)

[跟着源码学IM(十一)：一套基于Netty的分布式高可用IM详细设计与实现(有源码)-IM开发/专项技术区 - 即时通讯开发者社区! (52im.net)](http://www.52im.net/thread-4257-1-1.html)

[跟着源码学IM(三)：基于Netty，从零开发一个IM服务端-IM开发/专项技术区 - 即时通讯开发者社区! (52im.net)](http://www.52im.net/thread-2768-1-1.html)

[跟着源码学IM(八)：万字长文，手把手教你用Netty打造IM聊天-IM开发/专项技术区 - 即时通讯开发者社区! (52im.net)](http://www.52im.net/thread-3489-1-1.html)

[IM开发基础知识补课(五)：通俗易懂，正确理解并用好MQ消息队列-IM开发/专项技术区 - 即时通讯开发者社区! (52im.net)](http://www.52im.net/thread-1979-1-1.html)

