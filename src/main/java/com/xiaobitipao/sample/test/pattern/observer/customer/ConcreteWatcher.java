package com.xiaobitipao.sample.test.pattern.observer.customer;

public class ConcreteWatcher implements Watcher {

    @Override
    public void update(String str) {
        System.out.println(str);
    }
}
