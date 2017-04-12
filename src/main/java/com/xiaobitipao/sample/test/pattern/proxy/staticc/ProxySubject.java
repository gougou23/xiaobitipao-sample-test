package com.xiaobitipao.sample.test.pattern.proxy.staticc;

/**
 * 静态代理类
 */
public class ProxySubject implements Subject {

    private RealSubject realSubject = new RealSubject();

    @Override
    public void request() {
        System.out.println("before ...");
        realSubject.request();
        System.out.println("after  ...");
    }
}
