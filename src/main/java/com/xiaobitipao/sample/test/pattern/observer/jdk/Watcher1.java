package com.xiaobitipao.sample.test.pattern.observer.jdk;

import java.util.Observable;
import java.util.Observer;

// 观察者1
public class Watcher1 implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Watcher1 : count is " + arg);
    }
}
