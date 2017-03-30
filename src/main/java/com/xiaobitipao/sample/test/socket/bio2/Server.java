package com.xiaobitipao.sample.test.socket.bio2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // ServerSocket 监听的端口
    private final static int PORT = 8765;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORT);) {

            System.out.println("Server start ...");

            HandlerExecutorPool executorPool = new HandlerExecutorPool(50, 1000);
            while (true) {
                // server 尝试接收客户端 Socket 的连接请求
                // 通过 server 的 accept 方法进行阻塞
                Socket socket = server.accept();

                // 跟客户端建立好连接之后，就可以获取 socket 的 InputStream，并从中读取客户端发过来的信息
                // 新建一个线程执行客户端的任务
                executorPool.execute(new ServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
