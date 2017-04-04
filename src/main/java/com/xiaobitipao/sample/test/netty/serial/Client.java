package com.xiaobitipao.sample.test.netty.serial;

import java.io.File;
import java.io.FileInputStream;

import com.xiaobitipao.sample.test.utils.GzipUtils;

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

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                    sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                    sc.pipeline().addLast(new ClientHandler());
                }
            });

            ChannelFuture cf = b.connect(address, port).sync();

            for (int i = 0; i < 5; i++) {
                // 基本信息
                Req req = new Req();
                req.setId("" + i);
                req.setName("pro" + i);
                req.setRequestMessage("数据信息" + i);

                // 附件
                String path = System.getProperty("user.dir") + File.separatorChar + "sources" + File.separatorChar + "001.jpg";
                File file = new File(path);
                FileInputStream in = new FileInputStream(file);
                byte[] data = new byte[in.available()];
                in.read(data);
                in.close();
                req.setAttachment(GzipUtils.gzip(data));

                cf.channel().writeAndFlush(req);
            }

            // 等待客户端端口关闭
            cf.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Client().connect(ADDRESS, PORT);
    }
}
