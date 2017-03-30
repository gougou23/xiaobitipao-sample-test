package com.xiaobitipao.sample.test.socket.bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    // 要连接的服务端 IP 地址
    private final static String ADDRESS = "127.0.0.1";
    // 要连接的服务端监听端口
    private final static int PORT = 8765;

    public static void main(String[] args) {

        // 与服务端建立连接
        try (Socket client = new Socket(ADDRESS, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);) {

            System.out.println("Client start ...");

            // 向服务器端发送数据
            out.println("接收到客户端的请求数据 ...");

            // 设置接收数据的超时时间，单位是毫秒，这里设置 10s
            // 当设置的超时时间内 Socket 还没有接收到返回的数据的话，Socket就会抛出一个SocketTimeoutException
            client.setSoTimeout(10 * 1000);

            // 从服务器端接收数据
            String response = in.readLine();
            System.out.println("Client: " + response);

            System.out.println("Client end ...");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
