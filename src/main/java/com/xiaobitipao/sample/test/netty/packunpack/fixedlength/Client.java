package com.xiaobitipao.sample.test.netty.packunpack.fixedlength;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class Client {

    // 要连接的服务端 IP 地址
    private final static String ADDRESS = "127.0.0.1";

    // 要连接的服务端监听端口
    private final static int PORT = 8765;

    public void connect(String address, int port) throws Exception {

        // 客户端不需要 boss 线程组
        EventLoopGroup workgroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workgroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    // 设置定长字符串接收
                    sc.pipeline().addLast(new FixedLengthFrameDecoder(5));

                    // 设置字符串形式的解码
                    sc.pipeline().addLast(new StringDecoder());

                    sc.pipeline().addLast(new ClientHandler());
                }
            });

            ChannelFuture cf = b.connect(address, port).sync();

            cf.channel().writeAndFlush(Unpooled.wrappedBuffer("aaaaabbbbb".getBytes()));
            cf.channel().writeAndFlush(Unpooled.copiedBuffer("ccccccc".getBytes()));

            // 等待客户端端口关闭
            cf.channel().closeFuture().sync();
        } finally {
            workgroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Client().connect(ADDRESS, PORT);
    }
}
