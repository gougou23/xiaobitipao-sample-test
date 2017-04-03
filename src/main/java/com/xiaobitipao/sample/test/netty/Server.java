package com.xiaobitipao.sample.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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

            // 把俩个工作线程组加入进来
            sb.group(bossGroup, workerGroup);
            // 指定使用 NioServerSocketChannel 类型的通道
            sb.channel(NioServerSocketChannel.class);
            // 指定 TCP 连接的缓冲区大小
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            // 设置发送缓冲大小
            sb.option(ChannelOption.SO_SNDBUF, 32 * 1024);
            // 接收缓冲大小
            sb.option(ChannelOption.SO_RCVBUF, 32 * 1024);
            // 保持连接
            sb.option(ChannelOption.SO_KEEPALIVE, true);
            // sb.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 使用 childHandler 去绑定具体的 事件处理器
            sb.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(new ServerHandler());
                }
            });

            // 绑定指定的端口进行监听
            ChannelFuture f = sb.bind(port).sync();

            // 等待服务端监听端口关闭，即阻塞
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().bind(PORT);
    }
}
