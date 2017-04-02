package com.xiaobitipao.sample.test.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server implements Runnable {

    // Server 监听的端口
    private final static int PORT = 8765;

    // 1.多路复用器（管理所有的通道）
    private Selector seletor;

    // 2.建立读缓冲区
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);

    // 3.建立写缓冲区
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

    public Server(int port) {

        try {
            // 1.打开多路复用器
            this.seletor = Selector.open();

            // 2.打开服务器通道
            ServerSocketChannel ssc = ServerSocketChannel.open();

            // 3.设置服务器通道为非阻塞模式
            ssc.configureBlocking(false);

            // 4.绑定地址
            ssc.bind(new InetSocketAddress(port));

            // 5.把 ServerSocketChannel(服务器通道)注册到 Selector(多路复用器)上，并且监听阻塞事件
            ssc.register(this.seletor, SelectionKey.OP_ACCEPT);

            System.out.println("Server:服务端启动 -> 端口号：" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                // 1.让多路复用器开始监听
                this.seletor.select();

                // 2.返回多路复用器已经选择的结果集
                Iterator<SelectionKey> keys = this.seletor.selectedKeys().iterator();

                // 3.进行遍历
                while (keys.hasNext()) {
                    // 4.获取一个选择的元素
                    // 该元素(SelectionKey)代表 ServerSocketChannel 和 SocketChannel 向
                    // Selector 注册事件的句柄。
                    // 当一个 SelectionKey 对象位于 Selector 对象的 selected-keys 集合中时,
                    // 就表示与这个 SelectionKey 对象相关的事件发生了。
                    SelectionKey key = keys.next();

                    // 5.直接从容器中移除就可以了(只能手动移除)
                    keys.remove();

                    // 6.如果是有效的
                    if (key.isValid()) {
                        // 7.客户端连接就绪事件:等于监听 serversocket.accept() 返回一个 socket
                        if (key.isAcceptable()) {
                            System.out.println("Server:状态 -> 客户端连接就绪");
                            this.accept(key);
                        }

                        // 8.读就绪事件:表示输入流中已经有了可读数据, 可以执行读操作了
                        if (key.isReadable()) {
                            System.out.println("Server:状态 -> 读就绪");
                            this.read(key);
                        }

                        // 9.写就绪事件
                        if (key.isWritable()) {
                            System.out.println("Server:状态 -> 写就绪");
                            this.write(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {

        try {
            // 1.获取服务通道
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();

            // 2.执行阻塞方法
            SocketChannel sc = ssc.accept();

            // 3.设置阻塞模式
            sc.configureBlocking(false);

            // 4.注册到多路复用器上，并设置读取标识
            sc.register(this.seletor, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {

        try {
            // 1.清空缓冲区旧的数据
            this.readBuf.clear();

            // 2.获取之前注册的 socket 通道对象
            SocketChannel sc = (SocketChannel) key.channel();

            // 3.读取数据
            int count = sc.read(this.readBuf);

            // 4.如果没有数据
            if (count == -1) {
                key.channel().close();
                key.cancel();
                return;
            }

            // 5.有数据则进行读取,读取之前需要进行复位(把 position 和 limit 进行复位)
            this.readBuf.flip();

            // 6.根据缓冲区的数据长度创建相应大小的 byte 数组，接收缓冲区的数据
            byte[] bytes = new byte[this.readBuf.remaining()];

            // 7.接收缓冲区数据
            this.readBuf.get(bytes);

            // 8.打印结果
            String body = new String(bytes).trim();
            System.out.println("Server:从客户端读到的数据 -> " + body);

            // 9.读到数据后可以写回给客户端数据
            // ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key) {
        // ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        // ssc.register(this.seletor, SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) {
        new Thread(new Server(PORT)).start();
    }
}
