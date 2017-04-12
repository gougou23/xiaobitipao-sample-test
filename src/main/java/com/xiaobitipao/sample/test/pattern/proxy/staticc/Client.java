package com.xiaobitipao.sample.test.pattern.proxy.staticc;

public class Client {

    public static void main(String[] args) {

        Subject subject = new ProxySubject();
        subject.request();
    }
}
