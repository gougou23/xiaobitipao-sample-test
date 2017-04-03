package com.xiaobitipao.sample.test.netty;

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
        ByteBuf resp = Unpooled.copiedBuffer("来自 Server 的反馈信息".getBytes());
        ChannelFuture cf = ctx.write(resp);

        // 不加该设定，表示服务端和客户端保持永久的长连接，即客户端始终处于连接状态。
        // 加上该设定，表示服务端给客户端发送相应后断开与客户端的连接。
        cf.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将 write 的内容 flush
        // 或者直接调用 writeAndFlush 方法
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
