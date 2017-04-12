package com.xiaobitipao.sample.test.pattern.proxy.dynamic.customer;

public class RealSubject implements Subject {

    @Override
    public void request() {

        System.out.println("RealSubject is called.");
    }
}
