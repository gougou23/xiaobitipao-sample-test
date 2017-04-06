package com.xiaobitipao.sample.test.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
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

        EventLoopGroup workgroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workgroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                    sc.pipeline().addLast(new ClienHeartBeattHandler());
                }
            });

            ChannelFuture cf = b.connect(address, port).sync();

            cf.channel().closeFuture().sync();
        } finally {
            workgroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Client().connect(ADDRESS, PORT);
    }
}
