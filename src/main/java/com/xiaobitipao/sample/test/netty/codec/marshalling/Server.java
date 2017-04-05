package com.xiaobitipao.sample.test.netty.codec.marshalling;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Server {

    // 服务端监听端口
    private final static int PORT = 8765;

    public void bind(int port) throws Exception {

        // 1.第一个线程组:是用于接收 Client 端连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 2.第二个线程组:是用于处理已经接收到的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup);
            sb.channel(NioServerSocketChannel.class);
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            // 设置日志
            sb.handler(new LoggingHandler(LogLevel.INFO));
            sb.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel sc) throws Exception {
                    // 设置通过 Jboss Marshalling 进行序列化和反序列化
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());

                    // 超时handler（当服务器端与客户端在指定时间以上没有进行任何通信，则会关闭响应的通道，主要为减小服务端资源占用）
                    sc.pipeline().addLast(new ReadTimeoutHandler(5));

                    sc.pipeline().addLast(new ServerHandler());
                }
            });

            ChannelFuture cf = sb.bind(port).sync();

            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().bind(PORT);
    }
}
