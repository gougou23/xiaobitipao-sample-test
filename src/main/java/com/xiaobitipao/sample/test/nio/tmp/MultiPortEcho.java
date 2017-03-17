package com.xiaobitipao.sample.test.nio.tmp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiPortEcho {

    private int ports[];

    private ByteBuffer echoBuffer = ByteBuffer.allocate(1024);

    public MultiPortEcho(int ports[]) throws IOException {
        this.ports = ports;

        go();
    }

    private void go() throws IOException {

        // 异步 I/O 中的核心对象名为 Selector
        // Selector 就是注册对各种 I/O 事件的兴趣的地方，而且当那些事件发生时，就是这个对象告诉您所发生的事件。
        // 创建一个 Selector
        Selector selector = Selector.open();

        // Open a listener on each port, and register each one with the selector
        for (int i = 0; i < ports.length; ++i) {

            // 为了接收连接，需要一个 ServerSocketChannel
            // 事实上，要监听的每一个端口都需要有一个 ServerSocketChannel
            // 这里，对于每一个端口，打开一个 ServerSocketChannel
            ServerSocketChannel ssc = ServerSocketChannel.open();

            // 将 ServerSocketChannel 设置为非阻塞的
            // 必须对每一个要使用的套接字通道调用这个方法，否则异步 I/O 就不能工作
            ssc.configureBlocking(false);

            // 将 ServerSocketChannel 绑定到给定的端口
            ServerSocket ss = ssc.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            ss.bind(address);

            // 将新打开的 ServerSocketChannel 注册到 Selector上
            // 第二个参数是 OP_ACCEPT，这里它指定想要监听 accept 事件，也就是在新的连接建立时所发生的事件。
            // 这是适用于 ServerSocketChannel 的唯一事件类型。
            // register() 调用的返回值 SelectionKey 代表这个通道在此 Selector 上的这个注册。
            // 当某个 Selector 通知您某个传入事件时，它是通过提供对应于该事件的 SelectionKey 来进行的。
            // SelectionKey 还可以用于取消通道的注册。
            // SelectionKey key = ssc.register(selector,SelectionKey.OP_ACCEPT);
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Going to listen on " + ports[i]);
        }

        // 内部循环
        // 目前为止已经注册了我们对一些 I/O 事件的兴趣，下面将进入主循环。
        // 使用 Selectors 的几乎每个程序都像下面这样使用内部循环
        while (true) {

            // Selector 的 select() 方法会阻塞，直到至少有一个已注册的事件发生。
            // 当一个或者更多的事件发生时， select() 方法将返回所发生的事件的数量。
            // int num = selector.select();
            selector.select();

            // 获取发生了事件的 SelectionKey 对象的一个 集合
            // 通过迭代 SelectionKeys 并依次处理每个 SelectionKey 来处理事件。
            // 对于每一个 SelectionKey，您必须确定发生的是什么 I/O 事件，以及这个事件影响哪些 I/O 对象。
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();

            while (it.hasNext()) {

                SelectionKey key = (SelectionKey) it.next();

                // 检查发生了什么类型的事件
                if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                    // Accept the new connection
                    // 因为我们知道这个服务器套接字上有一个传入连接在等待，所以可以安全地接受它；
                    // 也就是说，不用担心 accept() 操作会阻塞：
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();

                    // 将新连接的 SocketChannel 配置为非阻塞的
                    sc.configureBlocking(false);

                    // Add the new connection to the selector
                    // 由于接受这个连接的目的是为了读取来自套接字的数据，
                    // 所以我们还必须将 SocketChannel 注册到 Selector 上
                    // SelectionKey newKey =
                    // sc.register(selector,SelectionKey.OP_READ);
                    sc.register(selector, SelectionKey.OP_READ);

                    // 删除处理过的 SelectionKey
                    // 如果没有删除处理过的键，那么它仍然会在主集合中以一个激活的键出现，这会导致我们尝试再次处理它。
                    it.remove();

                    System.out.println("Got connection from " + sc);
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                    // 当来自一个套接字的数据到达时，它会触发一个 I/O 事件。
                    // 这会导致在主循环中调用 Selector.select()，并返回一个或者多个 I/O 事件。
                    // 这一次， SelectionKey 将被标记为 OP_READ 事件

                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();

                    // Echo data
                    int bytesEchoed = 0;
                    while (true) {
                        echoBuffer.clear();

                        int r = sc.read(echoBuffer);

                        if (r <= 0) {
                            break;
                        }

                        echoBuffer.flip();

                        sc.write(echoBuffer);
                        bytesEchoed += r;
                    }

                    System.out.println("Echoed " + bytesEchoed + " from " + sc);

                    it.remove();
                }

            }

            // System.out.println( "going to clear" );
            // selectedKeys.clear();
            // System.out.println( "cleared" );
        }
    }

    public static void main(String args[]) throws Exception {

        if (args.length <= 0) {
            System.err.println("Usage: java MultiPortEcho port [port port ...]");
            System.exit(1);
        }

        int ports[] = new int[args.length];

        for (int i = 0; i < args.length; ++i) {
            ports[i] = Integer.parseInt(args[i]);
        }

        new MultiPortEcho(ports);
    }
}
