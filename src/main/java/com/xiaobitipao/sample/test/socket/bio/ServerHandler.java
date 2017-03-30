package com.xiaobitipao.sample.test.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);) {

            String body = null;
            while (true) {
                // 接收从客户端发送的数据
                body = in.readLine();
                if (body == null) {
                    break;
                }
                System.out.println("Server: " + body);
                out.println("服务器端回送响的应数据 ...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket = null;
        }
    }
}
