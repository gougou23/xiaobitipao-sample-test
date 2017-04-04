package com.xiaobitipao.sample.test.netty.serial;

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

public class Server {

    // 服务端监听端口
    private final static int PORT = 8765;

    public void bind(int port) throws Exception {

        // 1.第一个线程组:是用于接收 Client 端连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 2.第二个线程组:是用于处理已经接收到的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 3.一个启动 NIO 服务的辅助启动类,对 Server 进行一系列的配置
            ServerBootstrap sb = new ServerBootstrap();

            sb.group(bossGroup, workerGroup);
            sb.channel(NioServerSocketChannel.class);
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            // 设置日志
            sb.handler(new LoggingHandler(LogLevel.INFO));
            sb.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                    sc.pipeline().addLast(new ServerHandler());
                }
            });

            // 绑定指定的端口进行监听，同步等待成功
            ChannelFuture cf = sb.bind(port).sync();

            // 等待服务端监听端口关闭，即阻塞
            // 直到服务端连接关闭之后 main 方法才退出
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
