package com.xiaobitipao.sample.test.netty.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    // 要连接的服务端 IP 地址
    private final static String ADDRESS = "127.0.0.1";

    // 要连接的服务端监听端口
    private final static int PORT = 8765;

    public void connect(String address, int port) throws Exception {

        // 客户端不需要 boss 线程组
        EventLoopGroup workgroup = new NioEventLoopGroup();

        try {

            // 一个启动 NIO 服务的辅助启动类,对非服务端(客户端)的 channel 进行一系列的配置
            // 客户端的 SocketChannel 没有父 channel 的概念，所以不需要配置 childOption
            Bootstrap b = new Bootstrap();
            b.group(workgroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(new ClientHandler());
                }
            });

            // 客户端通过 connect 方法进行服务端连接
            ChannelFuture cf = b.connect(address, port).sync();
            cf.channel().writeAndFlush(Unpooled.copiedBuffer("来自 Client 的请求信息-1".getBytes()));
            cf.channel().closeFuture().sync();
            
            ChannelFuture cf2 = b.connect(address, port).sync();
            cf2.channel().writeAndFlush(Unpooled.copiedBuffer("来自 Client 的请求信息-2".getBytes()));
            cf2.channel().closeFuture().sync();
        } finally {
            workgroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Client().connect(ADDRESS, PORT);
    }
}
