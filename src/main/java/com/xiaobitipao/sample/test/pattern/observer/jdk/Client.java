package com.xiaobitipao.sample.test.pattern.observer.jdk;

import java.util.Observer;

public class Client {

    public static void main(String[] args) {

        Observer watcher1 = new Watcher1();
        Observer watcher2 = new Watcher2();

        BeginWatched beginWatched = new BeginWatched();
        beginWatched.addObserver(watcher1);
        beginWatched.addObserver(watcher2);

        beginWatched.counter(10);
    }
}
