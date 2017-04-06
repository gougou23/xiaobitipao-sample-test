package com.xiaobitipao.sample.test.netty.heartbeat;

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

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup);
            sb.channel(NioServerSocketChannel.class);
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.handler(new LoggingHandler(LogLevel.INFO));
            sb.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                    sc.pipeline().addLast(new ServerHeartBeatHandler());
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
