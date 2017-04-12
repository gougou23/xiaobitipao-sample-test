package com.xiaobitipao.sample.test.pattern.observer.jdk;

import java.util.Observable;
import java.util.Observer;

//观察者2
public class Watcher2 implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if (((Integer) arg).intValue() < 5) {
            System.out.println("Watcher2 : count is " + arg);
            // 取消关注
            // o.deleteObserver(this);
        }
    }
}
