package com.xiaobitipao.sample.test.netty.codec.marshalling;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class Client {

    // 要连接的服务端 IP 地址
    private final static String ADDRESS = "127.0.0.1";

    // 要连接的服务端监听端口
    private final static int PORT = 8765;

    private static class SingletonHolder {
        private static final Client instance = new Client();
    }

    public static Client getInstance() {
        return SingletonHolder.instance;
    }

    private EventLoopGroup group;
    private Bootstrap b;
    private ChannelFuture cf;

    private Client() {

        group = new NioEventLoopGroup();

        b = new Bootstrap();
        b.group(group);
        b.channel(NioSocketChannel.class);
        b.handler(new LoggingHandler(LogLevel.INFO));
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                // 设置通过 Jboss Marshalling 进行序列化和反序列化
                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());

                // 超时handler（当服务器端与客户端在指定时间以上没有进行任何通信，则会关闭响应的通道，主要为减小服务端资源占用）
                sc.pipeline().addLast(new ReadTimeoutHandler(5));

                sc.pipeline().addLast(new ClientHandler());
            }
        });
    }

    public void connect() {
        try {
            this.cf = b.connect(ADDRESS, PORT).sync();
            System.out.println("client:远程服务器已经连接, 可以进行数据交换 ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChannelFuture getChannelFuture() {

        if (this.cf == null || !this.cf.channel().isActive()) {
            this.connect();
        }

        return this.cf;
    }

    public static void main(String[] args) throws Exception {

        final Client client = Client.getInstance();
        client.connect();

        ChannelFuture cf = client.getChannelFuture();
        for (int i = 1; i <= 3; i++) {
            Request request = new Request();
            request.setId("" + i);
            request.setName("pro" + i);
            request.setRequestMessage("数据信息" + i);
            cf.channel().writeAndFlush(request);
            TimeUnit.SECONDS.sleep(3);
        }

        cf.channel().closeFuture().sync();

        // 为减小服务端资源占用，当服务器端与客户端在指定时间以上没有进行任何通信，则会关闭响应的通道
        // 为了确认这个点，这里在关闭以后重新连接并发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("进入子线程 ...");
                    ChannelFuture cf = client.getChannelFuture();
                    System.out.println("isActive=" + cf.channel().isActive());
                    System.out.println("isOpen=" + cf.channel().isOpen());

                    // 再次发送数据
                    Request request = new Request();
                    request.setId("" + 4);
                    request.setName("pro" + 4);
                    request.setRequestMessage("数据信息" + 4);
                    cf.channel().writeAndFlush(request);

                    cf.channel().closeFuture().sync();

                    System.out.println("子线程结束.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("断开连接,主线程结束 ...");
    }
}
