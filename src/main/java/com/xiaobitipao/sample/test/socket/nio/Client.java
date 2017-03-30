package com.xiaobitipao.sample.test.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

    // 需要一个Selector
    public static void main(String[] args) {

        // 创建连接的地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8765);

        // 建立缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 声明连接通道并打开
        try (SocketChannel sc = SocketChannel.open();) {

            // 进行连接
            sc.connect(address);

            while (true) {
                // 定义一个字节数组，然后使用系统录入功能：
                byte[] bytes = new byte[1024];
                System.in.read(bytes);

                // 把数据放到缓冲区中
                buf.put(bytes);

                // 对缓冲区进行复位
                buf.flip();

                // 写出数据
                sc.write(buf);

                // 清空缓冲区数据
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
