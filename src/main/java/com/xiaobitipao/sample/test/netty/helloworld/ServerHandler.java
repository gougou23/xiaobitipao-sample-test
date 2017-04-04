package com.xiaobitipao.sample.test.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "UTF-8");
        System.out.println("Server: " + request);

        // 写给客户端
        // 从性能角度考虑，为了防止频繁的唤醒 Selector 进行消息发送，
        // Netty 的 write 并不直接将消息写入 SocketChannel 中，
        // 调用 write 方法只是将待发送的消息放到发送缓冲区中，
        // 再通过调用 flush 方法，将发送缓冲区中的消息全部写到 SocketChannel 中
        ByteBuf resp = Unpooled.copiedBuffer("来自 Server 的反馈信息".getBytes());
        ChannelFuture cf = ctx.write(resp);

        // 不加该设定，表示服务端和客户端保持永久的长连接，即客户端始终处于连接状态。
        // 加上该设定，表示服务端给客户端发送相应后断开与客户端的连接。
        cf.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 通过调用 flush 方法，将发送缓冲区中的消息全部写到 SocketChannel 中
        // 或者直接调用 writeAndFlush 方法替换 write 方法
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
