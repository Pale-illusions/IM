package com.iflove.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author 苍镜月
 * @version 1.0
 * @implNote
 */
@Slf4j
@Configuration
public class NettyWebsocketServer {
    // websocket 运行端口
    public static final int WEB_SOCKET_PORT = 8090;

    public static final NettyWebSocketServerHandler NETTY_WEB_SOCKET_SERVER_HANDLER = new NettyWebSocketServerHandler();

    // boss 线程组，用于服务端接受客户端的连接
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    // worker 线程组，用于服务端接受客户端的数据读写
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 启动 Websocket 服务器
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        run();
        log.info("[start][Netty Server 启动在 {} 端口]", WEB_SOCKET_PORT);
    }

    /**
     * 优雅关闭服务器
     */
    @PreDestroy
    public void destory() {
        Future<?> future = bossGroup.shutdownGracefully();
        Future<?> future1 = workerGroup.shutdownGracefully();
        future.syncUninterruptibly();
        future1.syncUninterruptibly();
        log.info("[colse][Netty Server 关闭成功！]");
    }

    public void run() throws InterruptedException {
        // 服务器启动引导对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 设置服务器的连接队列大小为 128
                .option(ChannelOption.SO_BACKLOG, 128)
                // 开启 TCP 保活功能
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 为 bossGroup 添加 日志处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 30 秒 客户端未向服务端发送心跳则关闭连接
                        pipeline.addLast(new IdleStateHandler(30, 0, 0));
                        // 因为使用http协议，所以需要使用http的编码器，解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 以块方式写，添加 chunkedWriter 处理器
                        pipeline.addLast(new ChunkedWriteHandler());
                        // 多个 HTTP 数据段聚合成一个完整的请求或响应，直接处理完整的 HTTP 对象
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        // 获取 HTTP 请求的 IP、Token，保存在 channel 上
                        pipeline.addLast(new HttpHeadersHandler());
                        // 将 HTTP 协议 升级为 Websocket 协议，保持长连接
                        pipeline.addLast(new WebSocketServerProtocolHandler("/"));
                        // 自定义 Handler，处理业务逻辑
                        pipeline.addLast(NETTY_WEB_SOCKET_SERVER_HANDLER);
                    }
                });
        // 启动服务器，监听端口 8090
        serverBootstrap.bind(WEB_SOCKET_PORT).sync();
    }
}
