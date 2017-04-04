package com.xiaobitipao.sample.test.netty.packunpack.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
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
                    // 设置特殊分隔符
                    ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                    sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));

                    // 设置字符串形式的解码(ByteBuf -> String 变换)
                    sc.pipeline().addLast(new StringDecoder());

                    sc.pipeline().addLast(new ClientHandler());
                }
            });

            // 客户端通过 connect 方法进行服务端连接
            ChannelFuture cf = b.connect(address, port).sync();
            cf.channel().writeAndFlush(Unpooled.wrappedBuffer("aa$_".getBytes()));
            cf.channel().writeAndFlush(Unpooled.wrappedBuffer("bbbb$_".getBytes()));
            cf.channel().writeAndFlush(Unpooled.wrappedBuffer("cccccc$_".getBytes()));

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
