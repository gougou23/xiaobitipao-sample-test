
package com.xiaobitipao.sample.test.netty.heartbeat;

import java.util.HashMap;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHeartBeatHandler extends ChannelHandlerAdapter {

    /** key:ip value:auth */
    private static HashMap<String, String> AUTH_IP_MAP = new HashMap<String, String>();

    private static final String SUCCESS_KEY = "auth_success_key";

    static {
        AUTH_IP_MAP.put("169.254.103.162", "1234");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof String) {
            auth(ctx, msg);
        } else if (msg instanceof RequestInfo) {
            RequestInfo info = (RequestInfo) msg;
            System.out.println("--------------------------------------------");
            System.out.println("当前主机 ip 为: " + info.getIp());
            System.out.println("当前主机 cpu 情况: ");
            HashMap<String, Object> cpu = info.getCpuPercMap();
            System.out.println("cpu 总使用率: " + cpu.get("combined"));
            System.out.println("cpu 用户使用率: " + cpu.get("user"));
            System.out.println("cpu 系统使用率: " + cpu.get("sys"));
            System.out.println("cpu 等待率: " + cpu.get("wait"));
            System.out.println("cpu 空闲率: " + cpu.get("idle"));
            System.out.println("当前主机 memory 情况: ");
            HashMap<String, Object> memory = info.getMemoryMap();
            System.out.println("memory 内存总量: " + memory.get("total"));
            System.out.println("memory 当前内存使用量: " + memory.get("used"));
            System.out.println("memory 当前内存剩余量: " + memory.get("free"));
            System.out.println("--------------------------------------------");

            ctx.writeAndFlush("info received!");
        } else {
            ctx.writeAndFlush("connect failure!").addListener(ChannelFutureListener.CLOSE);
        }
    }

    private boolean auth(ChannelHandlerContext ctx, Object msg) {

        // System.out.println(msg);
        String[] ret = ((String) msg).split(",");
        String auth = AUTH_IP_MAP.get(ret[0]);
        if (auth != null && auth.equals(ret[1])) {
            ctx.writeAndFlush(SUCCESS_KEY);
            return true;
        } else {
            ctx.writeAndFlush("auth failure !").addListener(ChannelFutureListener.CLOSE);
            return false;
        }
    }
}
