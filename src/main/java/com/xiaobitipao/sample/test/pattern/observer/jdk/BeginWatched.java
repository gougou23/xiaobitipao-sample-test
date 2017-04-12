package com.xiaobitipao.sample.test.pattern.observer.jdk;

import java.util.Observable;

// 主题对象：Observable的子类
public class BeginWatched extends Observable {

    public void counter(int number) {
        for (; number >= 0; number--) {
            this.setChanged();
            this.notifyObservers(number);
        }
    }
}
